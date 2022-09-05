
## Project setup

- First Open the project in intellij
- Next run the server module and then run the client module

There are two modules in the project:
    - client : To run the application client select BookReviewApplicationClient class and run as Springboot application
    - server : To run the application server select BookReviewApplicationServer class and run as Springboot application

## Running Unit Tests

- Click on the java folder under tests and click on Run All Tests

### Command Line arguments you can choose when running application client
- --help Output a usage message and exit
- --search _TERMS_ Search the Goodreads' API and display the results on screen.
- --sort _FIELD_ where field is one of "author" or "title"
- --host _HOSTNAME_ the hostname or ip address where the server can be found, should default to 127.0.0.1
- --page _NUMBER_ the page number in the result set
- --size _NUMBER_ the page size in the result set