# Contribution rules

## Branching strategy

### Fix an issue

If you intend to fix an issue, for example issue \#13 \[Task] Gestion des SMS. First, create a new branch from master,
named `13-gestion-des-sms`. Then, when you are done, create a pull request to merge your branch into master.
If changes have occurred between the creation of your branch and the creation of your pull request, you will have to
rebase your branch on master.

## Development flow

Once a pull request is created, the tests are automatically launched. If they fail, you will have to fix the problem
before the pull request can be merged.

## Code style

The code style is enforced by the .editorconfig configuration files. You can import them in your IDE to have the same
code style as the rest of the project.