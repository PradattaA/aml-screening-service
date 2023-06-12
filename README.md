# aml-screening-service

This is a simple aml-screening-service

# The Background
Banks are responsible for stopping money laundering. Hence it is necessary to avoid money transfers to terrorists and criminals. The list of such persons is published by EU:  
http://eeas.europa.eu/cfsp/sanctions/consol-list/index_en.htm

# Task
Your task is to implement simple webservice to compare given name against sanctioned list to detect such transfers.  
Your API should have following functionality:

- Webservice with name as input and it should return if name match
  exists. True/false and extra data if needed.
- The webservice should be able to handle noisewords (example: the, to,
  an, mrs, mr, and) that need to be ignored.
- The webservice should detect names with spelling errors and also
  return as few "false positive" matches as possible.
- Webservices to    add, update and remove sanctioned names.

# Technical requirements
Implement using **Java** or **Kotlin** and **Spring framework**.  
Sanctioned names should be stored in a database. (In-memory database can be used)  
Open source libraries can be used.  
Source code should be stored in a git based version control system.
# Example
There is Sanctioned name: "Osama Bin Laden". Out of the box your API should detect at least the following user entries:
- "Osama Laden"
- "Osama Bin Laden"
- "Bin Laden, Osama"
- "Laden Osama Bin"
- "to the osama bin laden"
- "osama and bin laden"

In reality, name matching is not a trivial task. Matches could happen also based on:

- Substring matches - sl: "Robert", user: "Bert"
- Ignoring spelling errors - sl: "Madis", user: "Madus"
- Abbreviations - sl: "Joe Luis Webb", user: "Joe L. Webb"
- Noisewords - sl: "Mr. John Smith" user: "John Smith"

# Instructions
This test can be either completed at home or during a pair programming exercise.  

**When implementing at home:**  
You should aim to spend no longer than 4 hours on your solution.  
We would rather you complete less functionality to a good standard than rushing to try to complete it all.  

Show us your workings by initialising a git repo and committing your changes as you go.  
Your submission should be considered the quality you would expect to open a PR, not to merge one. We don't expect the submission to be production standard or gold plated.  

Submit your sample by bundling your git repo (git bundle create name.bundle --all) and emailing the bundle to us (via your recruiter if you have one).  
The submission will be followed up with a review call where we will ask you to walk us through your solution. We will then ask you to extend it with some additional functionality.  

**When implementing in a Pairing session:**  
We want to mimic real life as much as possible. This means that candidate should be able to google or ask questions.  
We will spend 5 minutes introducing the challenge, 40 minutes coding and 15 minutes discussing the solution.

# How to run
 Install PostgreSQL

```bash
$ brew install postgresql
```

Start PostgreSQL service

```bash
$ brew services start postgresql # or "brew services run postgresql" to have it not restart at boot time
```

Run `AmlScreeningServiceApplication` with VM Option 

```agsl
-Dspring.profiles.active=development
```

# Service design and planning notes

### Service component and data flow

![IMG_0003](https://github.com/neelhridoy/aml-screening-service/assets/79056702/b93a7714-85c2-4a17-a5c0-3750254b5021)


### Fuzzy Lookup from DB Logic
![IMG_0004](https://github.com/neelhridoy/aml-screening-service/assets/79056702/591ff402-e1de-45d2-b6e8-afe783c296ce)

$ brew install postgresql
