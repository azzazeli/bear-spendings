# bear-spendings
Simple app that monitor bear spendings. 

##Features (version 0.1)
 - Simple display home page
 - Add new bill form
 - List fo bills (including bill items)


### Simple home page
    A welcome page for the user. Nothing fancy. Wil be enhanced in upcoming version
### Add new bill form
    The main scope of app is to create an easy way to introduce bill. First implementation will be simple, but
    is going to cover the main case.

   Example
   * select bill date
   * provide a store
   * a list of top products from selected store is displayed
   * select one of top product. Bill iotem form will be populated with product name and price, quantity set to 1.
   User can edit all these fields.
   * add bill items to the bill.
    

## Technologies
On UI side angular 6 is used. Server side will be implemented with spring boot 2.0
H2 database will be used for this version
In memory authentication wil be enough with hardcoded user name and pswd.

### Run build with no UI test
UI test take some times to execute. There are cases when it is necessary to run build without them.
For this case run following command:
```gradlew clean build -Dskip.ui.test=true``` 

