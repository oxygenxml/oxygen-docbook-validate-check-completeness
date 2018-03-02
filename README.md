# DocBook Validation and Completeness Check 
Oxygen XML plugin which allows validation of DocBook files reporting broken images, internal links, external links or profile conditions that conflict with profiling preferences.

## Installation


This add-on is compatible with Oxygen XML Editor (or XML Author) version 19.0 or higher. 

To install the add-on in Oxygen XML Editor/Author, follow these instructions:

1. Go to **Help->Install new add-ons** to open an add-on selection dialog box.
2. Enter or paste https://raw.githubusercontent.com/oxygenxml/docbook-validate-check-completeness/master/build/addon.xml in the **Show add-ons from** field.
3. Select the **DocBook Checker** add-on and click **Next**.
4. Select the **I accept all terms of the end user license agreement** option and click **Finish**.
5. Restart the application.

Result: A **Check DocBook for Completeness** action will now be available on the toolbar and in the contextual menu. This action opens a dialog box that offers various validation options for running a completeness check on the current DocBook document.


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
* **Check for broken links to external web sites** : Check if links from your DocBook document to external resources (eg: websites) are broken.
* **Check for broken links to internal targets** : Validate the cross references.
* **Check for references to missing images** : Check for broken references to images.
* **Report attributes and values that conflict with profiling preferences**:
Looks for profiling attributes and values that are not defined in the [Profiling / Conditional Text](https://www.oxygenxml.com/doc/versions/19.0/ug-author/topics/preferences-profiling-conditions.html#preferences-profiling-conditions) preferences page
* **Generate resource hierarchy report**: After the validation will be complete, a dialog box that contains a report with all used resources will be showed. In this dialog, you can do quick search and save the report in HTML format.   

Copyright and License
---------------------
Copyright 2018 Syncro Soft SRL.

This project is licensed under [Apache License 2.0](https://github.com/oxygenxml/oxygen-docbook-validate-check-completeness/blob/master/LICENSE)
