# UML: Student 1

Two separate class diagrams matching the team processing diagrams:

- builder.png / builder.svg: ProcessingOptions.Builder, ProcessingOptions and CaseMode.
- factory-method.png / factory-method.svg: ExportJob, concrete jobs, exporters and FileService.
- The .puml files are editable PlantUML sources.

UML (Unified Modeling Language) is a standard visual notation for describing software. Class boxes show fields and methods; arrows show dependencies, inheritance and interface implementation.

Builder creates immutable processing settings. Factory Method lets ExportJob use a TextExporter selected by the concrete job's overridden createExporter() method.

ProcessingOptions.Builder is a static nested class. Record-generated equals/hashCode and the constructor signature are omitted. File and export methods can throw IOException; declarations are omitted for readability.

Place these files in docs/student1/.
