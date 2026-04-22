# Code Attribute (AKA in code references)

When referencing code or other resources that you used for a piece of code, it is important to cite the work.

In this repository there in code citations follow a specific format.

A citation is contained within a **kotlin comment** or any language comment.

## Citation comment format

To denote a citation block use `BEGIN-CITATIONS <internal-citation-name>` and `END-CITATIONS`.

An `<internal-citation-name>` is a semi-unique name that idenitfies a citation block/s. The reason it semi-unique is that it is used to group related references together. For instance, if for a piece of code you used both w3schools css documentation and w3schools javascript documentation you have two sets of BEGIN and END CITATIONS which would have the same internal-name, but different contents.

Contained within a citation block is the following:

- `LINK` - the website or resource of the reference material.
- `DESC` - A comment that explains the piece of code that you have written (not an explaination of the reference)

    Example:

    ```kotlin
    // Use TextField instead of TextEdit
    val textfield = new TextField();
    ```
    : Before referencing

    ```kotlin
    // BEGIN-CITATIONS text-field
    // LINK www.someandroidblog.com/blog/why-use-textfield-insted-of-textedit
    // DESC Use TextField instead of TextEdit
    // END-CITATIONS
    val textfield = new TextField();
    ```
    : After referencing
- `ACCESSED` - The time and date you accessed the resource.

    Please use [ISO Date formatter](https://dencode.com/date/iso8601?v=now&tz=Africa%2FJohannesburg&decimal-separator=.) to get a properly formated date. In the textbox type in `now` and copy the `ISO8601 Date` options.

    Date example: `20260421T174234.178228780+0200` you can omit the numbers between the `.` and the `+`

    Example:

    ```kotlin
    // BEGIN-CITATIONS text-field
    // LINK ...
    // DESC ...
    // ACCESSED 20260421T174234.178228780+0200
    // END-CITATIONS
    val textfield = new TextField();
    ```
- `CSL-REF` - A unique identifier for this reference, *must be unique*. Spaces must be replace with `-`

## Complete Examples

These references were ones I used in real code, though it was before I added `ACCESSED`.

```csharp
// BEGIN-CITATIONS custom-attributes
// LINK https://learn.microsoft.com/en-us/dotnet/csharp/advanced-topics/reflection-and-attributes/accessing-attributes-by-using-reflection
// DESC Easing the burden of accessing the resource by defining it through code
// CSL-REF custom-attrs
// END-CITATIONS
[System.AttributeUsage(System.AttributeTargets.Property | AttributeTargets.Field)]
public class ManifestResourceAttribute : System.Attribute {
    private string ResourceName;

    public ManifestResourceAttribute(string resourceName) {
        ResourceName = resourceName;
    }

    public string GetResourceName() {
        return ResourceName;
    }
}
```
: Single reference citation

```csharp
// BEGIN-CITATIONS vba-and-dotnet
// LINK https://learn.microsoft.com/en-us/dotnet/api/microsoft.visualbasic.interaction.inputbox?view=net-9.0
// DESC use visual basic functionality in a c# as both run on the CLR and are compatible with each other
// CSL-REF msdocsVBAInteractionInputBox
// END-CITATIONS
// BEGIN-CITATIONS vba-and-dotnet
// LINK https://stackoverflow.com/a/97156
// DESC What is the C# version of VB.NET's InputBox?
// CSL-REF stackexchangeAnswerUseInputboxFromVB
// END-CITATIONS

// It turns out that c# api do not contain a InputBox but VisualBasic has an api all the way back from
// when it was a windows automation scripting language, now that it has been integrated into the CLR,
// any CLR language may integrate it and consume its api.
var text = Interaction.InputBox(prompt, prompt);
```
: Multiple references citation (note the same `internal-name`)

## How it works, Automation

I wrote I simple python script searches all code files for citation blocks.

It collects all the citations grouping them by `internal-name` and outputs a json file
containing the references and a markdown file containing all the citation groups.

The json and markdown are feed into another program along with csl stylesheet that describes
the referencing style eg. Harvard Anglia, or normal Harvard and outputs a pdf.

I have used this tool for a number of years, and it works. As long as citations are formatted correctly,
I just need to run the tool on my end or even setup a github actions to regenerate it.
