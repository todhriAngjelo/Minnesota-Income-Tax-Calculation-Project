# Minnesota-Income-Tax-Calculation-Project
## Project Goal
The goal of this project is to reengineer a legacy Java application. At a glance, serves for the income tax calculation of the Minnesota state citizens. The tax calculation accounts for the marital status of a given citizen, his income, and the amount of money that he has spend, as witnessed by a set of receipts declared along with the income. The legacy application takes as input txt or xml files that contain the necessary data for each citizen. The tax calculation is based on a complex algorithm provided by the Minnesota state. The application further produces graphical representation s of the data in terms of bar and pie charts. Finally. the application produces respective output reports in txt or xml.

### Reengineering
- We fixed all the Smells based on project backlog guidelines.
- We redesigned InputSystem, OutputSystem, Database to 2 key manager classes names FilesManager and ChartsManager
- We introduced model, view, manager packages for the 3 key modules of our project
- We used java 8 streams and lambdas for recoding some old-fashioned fors and ifs
- Models were refactored based on simple POJO's philosophy. Removed heave logic from models
- Static code analysis tool ( sonarLint ) was used to fix many common java mistakes

## Contributors
| Name  | AM | Email |
| ------------- | ------------- | ------------- |
| Apostolis Kasselouris  | 2994 | kasseluris.apostolos@gmail.com |
| Todhri Angjelo | 3090 | todhri_angjelo@outlook.com.gr |

## Reengineering the legacy code
The goal of this project is to reengineer a legacy Java application. At a glance, serves for the 
income tax calculation of the Minnesota state citizens. The tax calculation accounts for the marital
status of a given citizen, his income, and the amount of money that he has spend, as witnessed by a
set of receipts declared along with the income. The legacy application takes as input txt or xml 
files that contain the necessary data for each citizen. The tax calculation is based on a complex 
algorithm provided by the Minnesota state. The application further produces graphical representation
s of the data in terms of bar and pie charts. Finally. the application produces respective output 
reports in txt or xml.

Legacy code Smells:
- The Taxpayer class has a lot of code duplication.

- The Taxpayer class suffers from [primitive obsession](https://refactoring.guru/smells/primitive-obsession). 
A lot of constants for the income limits and income tax rates, are used to calculate the tax of the Taxpayer. 
The values of these constants depend on the value of the family status attribute. Essentially the family status acts 
as a typecode for different kinds of Taxpayers.

- The different subclasses of Receipt are [lazy classes](https://refactoring.guru/smells/lazy-class). 

- The Database, the InputSystem and the OutputSystem classes do not really follow the object-oriented style. 
The main problem is that all of them just define a bunch of static methods that operate on static class attributes. 
All these static methods and attributes prevent further refactoring that will allow to separate the class responsibilities 
into smaller classes and get rid of code duplication by extracting abstract classes and template methods.

- The InputSystem class has too many responsibilities. It is used to parse two different formats (XML and TXT).

- The OutputSystem class has too many responsibilities. It is used to generate reports in two different formats. 
Moreover, it creates the graphical representation of the data (pie charts, bar charts and so on).

## TODOS
