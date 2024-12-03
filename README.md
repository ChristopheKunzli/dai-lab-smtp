# SMTP Prank Client Report

## Overview

This project is a TCP client application in Java designed to automatically send prank emails to groups of victims by leveraging the SMTP protocol at the wire level using the Socket API. It aims to demonstrate an implementation of SMTP in a practical and entertaining way.

## Mock SMTP Server Setup

To test the application, you can use a mock SMTP server called MailDev. Follow these steps to set it up:

1. Install Docker.
2. Run the following command to start MailDev:
```sh
docker run -d -p 1080:1080 -p 1025:1025 maildev/maildev
```
The SMTP server will be accessible at localhost:1025 and the MailDev web interface at http://localhost:1080.

## Configuring and Running the Prank Campaign

### Clone the repository with this command:  
- with http:  
  - git clone https://github.com/ChristopheKunzli/dai-lab-smtp.git  
- or ssh:  
  - git clone git@github.com:ChristopheKunzli/dai-lab-smtp.git  

### Edit the configuration files to define:
Victims list (e-mail addresses) : src/victims.txt
Messages list (subjects and bodies) : src/messages.txt
Number of groups for the prank campaign : not a file, must be passed in parameter
Run the application using the following command:

``` sh
java -jar SMTPPrankClient.jar <victimsList.txt> <messages.txt> <number of groups>
```
## Implementation Description
### Class Diagram

A simplified class diagram showing the core components of the prank client will be included here.
Key Components

    SMTPClient: Manages the socket communication with the SMTP server.
    PrankGenerator: Generates prank groups and messages.
    ConfigManager: Loads and validates configuration files.

Example Dialogue with SMTP Server

Below is an example of a dialogue between the client and the SMTP server during an email prank (screenshot included):

``` sh
C: EHLO localhost
S: 250 OK
C: MAIL FROM:<bob@example.com>
S: 250 OK
C: RCPT TO:<alice@example.com>
```