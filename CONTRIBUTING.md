# Contributing to Rectball

If you want to help to the development of Rectball, please read this file. This file has been designed to avoid any kind of error or miscomunication.

This document is structured in two sections. The first section will explain what can you do to contribute to Rectball. The second will explain what tools you have to contribute to Rectball and how to use them.

## What can you do?

### Suggest new features

You can suggest new ideas for the game. There is no guarantee that your idea will be implemented, because implementing new features are a investment, the features that make more sense for the game are the ones that have more chances to be implemented. Thus, before reporting ideas think on whether your idea makes sense or not.

### Reporting bugs

If you see an anomaly during the execution of Rectball, you can report a bug. Please, report bugs that are reproducible. The developers need to be able to force the bug to happen in order to solve it.

Before reporting a bug, please follow these rules.

1. Make sure that the bug hasn't already been reported. Check the issue tracker to find out if someone has already reported this bug. Duplicated bug reports will be closed.
2. Make sure you aren't running an outdated version of Rectball. If you are running the desktop version of Rectball, make sure the version is not outdated. If you are using the Android version, check if there are any updates for your game at the store.
3. If you have the technical knowledge, fetch the latest **development** version and try to reproduce the bug with the code that is on `master` branch to see if this bug has already been fixed for the next release.

If you are still committed to report a bug, make sure you report the following:

* What happened when the bug happened.
* What steps can be followed to reproduce the bug.
* What did you expect to happen.
* Hardware information: if it's only related to Android, what is your OS version. If it's only related to Desktop, what is your operating system.

### Contributing code

If you have the knowledge, you can contribute code for adding new features or fixing bugs.

If you contribute code, you will be considered a *contributor* and your name and GitHub profile will be added to the CONTRIBUTORS file in the root folder of this repository.

The code that you contribute to this project has your own copyright because it is yours. Thus, when contributing code to the project you agree to license your contributed code under the same LICENSE than the rest of the project.

There is no official style guide for this project. Just try to follow the same style than the code surronding your code.

## How to contribute

### Reporting bugs

Report your bug at the issue tracker. Before reporting your bug, take some time to see if there is already a report for that bug using the search feature. Duplicated bug reports will be closed and marked as duplicate.

Please, include as much information as you can, based on the guidelines provided above. If possible, even provide a screenshot.

Although it is not strictly required, it is encouraged to report your bugs in English. This helps with the project internationalization.

### Suggesting ideas

Report your idea at the issue tracker. It will be labeled as *enhancement* to distinguish your issue from *bug* issues. GitHub doesn't let you do this, one of the project maintainers have to add the tag, which might take some time depending on their availability.

### Contributing code

Use a standard pull request.

* Fork the project.
* Make your changes in your forked project.
* Send a pull request on GitHub.

Please, send your pull request to the correct branch. This project uses the feature branch workflow. Use the following guide.

If your contribution is related to a feature being implemented at the moment, please use that feature branch as your base branch.

> Example: I'm working on a feature and while is not finished it is pushed at `feature/my-work`. You want to contribute code to that feature. Send your pull request using `feature/my-work` as your base branch. Don't use `master` since the feature is still not merged in `master`.

If your contribution is a general fix or something that is NOT part of a feature currently being implemented, use `master` as your base branch.

There is no `master`/`dev` split in this project. `master` always contain the latest development code. If you want a stable version, use the project tags.