# DocBook Validation and Completeness Check 
Oxygen XML plugin which allows validation of Docbook files reporting broken images, internal links, external links or profile conditions that conflict with profiling preferences.

## Installation


This add-on is compatible with Oxygen XML Editor (or XML Author) version 19.0 or higher. 

You can install the add-on by using Oxygen's add-ons support. In Oxygen, go to **Help->Install new add-ons...** and use this add-on repository URL:

https://raw.githubusercontent.com/oxygenxml/docbook-validate-check-completeness/master/build/addon.xml

then continue the installation process.


## Validating a DocBook document

1. Click the **Check DocBook for Completeness** button on the toolbar or in contextual menu to open the **DookBook Completeness Check** dialog box.
2. Choose either to validate the current edited file or to validate a set of other documents.
3. If you are using profiling, check the **Use profile conditions (filters)** box and select the appropriate option.
4. Select any other options you want to check.
5. Click **Check** to run the validation process.

## Configuring the DocBook validation
You can configure the validation process with the following options that are available in the **DookBook Completeness Check** dialog box:

* **Use profile conditions (filters)** :
The content of the document is filtered by applying a [profiling condition set](https://www.oxygenxml.com/doc/versions/19.0/ug-author/topics/preferences-profiling-conditions.html#preferences-profiling-conditions) before validation. 
You can choose between the following options:
   + **From condition set**: The document is filtered using the conditions from the table below. Use the **Add** or **Remove** buttons to configure the table. The Add button opens a dialog box that allows you to select conditions from a tree. In this dialog you find the **"Learn conditions"** button, that will show you only the conditions used in the document.
  + **From all available condition sets**:  For each available condition set, the document content is filtered using that set before validation.
* **Check external links** : Check if links from your DocBook document to external resources (eg: websites) are broken.
* **Check internal links** : Validate the cross references.
* **Check images** : Check for broken references to images.
* **Report attributes and values that conflict with profiling preferences**:
Looks for profiling attributes and values that are not defined in the [Profiling / Conditional Text](https://www.oxygenxml.com/doc/versions/19.0/ug-author/topics/preferences-profiling-conditions.html#preferences-profiling-conditions) preferences page
* **Generate resource hierarchy report**: After the validation will be complete, a dialog box that contains a report with all used resources will be showed. In this dialog, you can do quick search and save the report in HTML format.   