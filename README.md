# Docbook-Validate-Check-Completeness
Oxygen XML plugin which allows validation of Docbook files reporting broken images, internal links, external links or profile conditions that conflict with profiling preferences.

## Installation


This add-on is compatible with Oxygen XML Editor (or XML Author) version 17.0 or higher. 

You can install the add-on by using Oxygen's add-ons support. In Oxygen, go to Help->Install new add-ons... and paste:

https://raw.githubusercontent.com/oxygenxml/docbook-validate-check-completeness/master/build/addon.xml

and continue the installation.


## Validating a DocBook:

1. Click the **Check DocBook for Completeness** button on the toolbar or in contextual menu to open the **DookBook Completeness Check** dialog box.
2. Choose from check current file or to add others.
3. If you are using profiling, check the **Use profile conditions(filters)** box and select the appropriate option.
4. Select any other options you want to check.
5. Click **Check** to run the validation process.

## Configure the DocBook validation:
You can configure the validation process with the following options that are available in the DookBook Completeness Check dialog box:

* Use profile conditions(filters) :
The content of the document is filtered by applying a [profiling condition set](https://www.oxygenxml.com/doc/versions/19.0/ug-author/topics/preferences-profiling-conditions.html#preferences-profiling-conditions) before validation. 
You can choose between the following options:
   + From condition set: The document is filtered using the conditions from the table below. Use the Add or Remove buttons to configure the table. The Add button opens a dialog box that allows you to select conditions from a tree. In this dialog you find the "Learn conditions" button, that will show you only the conditions used in document.
  + From all available condition sets:  For each available condition set, the document content is filtered using that set before validation.
* Check external links : Validate the links from your DocBook document to websites.
* Check internal links : Validate the cross references.
* Check images : Validate the images in your document.
* Report attributes and values that conflict with profiling preferences:
Looks for profiling attributes and values that are not defined in the [Profiling / Conditional Text](https://www.oxygenxml.com/doc/versions/19.0/ug-author/topics/preferences-profiling-conditions.html#preferences-profiling-conditions) preferences page