import asyncio
import sys
import re
from typing import Generator
import json
import subprocess
from io import StringIO
from pathlib import Path, PosixPath, WindowsPath
from collections import defaultdict
from datetime import datetime
import sys

loop = asyncio.new_event_loop()


def make_citation(*, link: str, desc: str, file: Path, line: tuple[int, int], accessed: datetime, csl: str = None):
    if csl is None:
        csl = ""
    else:
        csl = "@"+csl
    return dict(file=file, lines=line, link=link, description=desc, accessed=accessed, csl=csl)


def generate_references(text):
    proc = subprocess.run(["pandoc", "-C", "-t", "markdown-citations-fenced_divs",
                          "-o", "REFERENCE.md"], input=text, text=True)


def get_line(marker, line: str) -> str:
    start_marker = line.find(marker)
    # This should be the word BEGIN-CITATIONS |--> [word] till \n
    word = line[start_marker+len(marker):].strip()
    return word


async def parse_file(path: Path) -> Generator[tuple[str, dict], None, None]:
    citation_name = None
    citation = {}
    in_citation = False
    with path.open() as f:
        for lno, line in enumerate(f):
            if "BEGIN-CITATIONS" in line:
                in_citation = True
                citation = {}
                citation_name = get_line("BEGIN-CITATIONS", line)
                citation["line"] = [lno, None]
                citation["file"] = path
            if in_citation and "LINK" in line:
                citation["link"] = get_line("LINK", line)
            if in_citation and "CSL-REF" in line:
                citation["csl"] = get_line("CSL-REF", line)
            if in_citation and "DESC" in line:
                citation["desc"] = get_line("DESC", line)
            if in_citation and "ACCESSED" in line:
                citation["accessed"] = datetime.fromisoformat(get_line("ACCESSED", line))
            if in_citation and "END-CITATIONS" in line:
                citation["line"][1] = lno
                in_citation = False
                try:
                    yield citation_name, make_citation(**citation)
                except TypeError as e:
                    print('error', citation, file=sys.stderr)
                    break
                citation_name = None


def default_dump(o) -> str:
    if isinstance(o [Path, PosixPath, WindowsPath]):
        return str(o)
    if isinstance(o, datetime):
        return [[str(o.year), o.month, o.day]]
    return o


async def main():
    base_dir = Path(__file__).parent
    paths = base_dir.glob("**/*.kt") # find all kotlin files

    citations: dict[str, list] = defaultdict(list)

    for path in paths:
        async for name, citation in parse_file(path):
            citations[name].append(citation)

    text = StringIO()

    print("---", file=text)
    print("bibliography: csl_references.json", file=text)
    print("csl: harvard-anglia-ruskin-university.csl", file=text)
    print("citeproc: true", file=text)
    print("---", file=text)

    for name, cites in citations.items():
        print(file=text)
        print("##", name, file=text)
        print(file=text)
        for cite in cites:
            print("-", cite["description"], file=text)
            # print()
            # print(" "*3, "<" + cite["link"] + ">")
            print(file=text)
            print(" "*3, "Reference:", cite["csl"], file=text)
            print(file=text)
            print(" "*3, "[{file}]({file})".format(file=str(cite["file"])),
                  "line:", '-'.join(map(lambda x: str(x), cite["lines"])), file=text)

    with Path("references.json").open("w") as f:
        json.dump(citations, f, default=default_dump)

    value = text.getvalue()
    print(value)
    generate_references(value)


loop.run_until_complete(main())
