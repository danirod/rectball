# Versioning of Rectball

I realised that semantic versioning is a dumb idea for applications like this.
This is not a JavaScript library where it is important to manage the stability
of some functions, this is just a game, and the final user will not care about
the stability of the system architecture of your game, but about the things
that the user can actually see.

Therefore, starting with v0.5, I am dropping the old semantic versioning system
and using a more simple and practical approach. A version is still composed
of three numbers, vX.Y.Z, but they have a different purpose:

- X is the major version, it will stay as 0 until I consider the game is "done"
  and then I will bump it to 1. Then, it will stay like that unless I ever
  rewrite the game, then it would bump to 2. However, I doubt that is going to
  happen anytime soon.

- Y is the minor version. It is usually related to the sprint iteration, but
  some minor releases are used for development while others are used for
  bug fixing.

- Z is the build number. This is a monotonic number that increases
  chronologically and never resets. Therefore, a release A is newer than a
  release B if the build number for A is higher than the build number of B.
  The bump policy is intentionally not documented and left as an implementation
  detail. Currently, it increments whenever I push code to GitHub and let the
  CI test and build binary versions of the application.

Bumping the major and minor version usually is done one by one, which means
that 0.6 is followed with 0.7; a rewrite of 1.5 is versioned as 2.0, and so on.
Build numbers, on the other hand, may bump one or more numbers at once. For
instance, if the CI bumps the build number to 345, but I immediately commit and
push something else, CI will bump again the version number to 346. I may only
release the later one, so the next version after 344 will be 346, not 345.

## Plan for v0.6

First version using the new system is v0.5.436. All the versions in the v0.5
family that have a build number higher than 436 are either additional releases
for PC and web, or bug fixes.

Once the identified list of bugs is finished, the minor version will be bumped
to v0.6. This means that v0.6 will not contain new features, only bug fixes
that were present when v0.5 was released.

## Stable channel and development channel

Once v0.6 is released, version families will be grouped into two channels:
stable or development. This approach is inspired by an older versioning scheme
of the Linux kernel.

v0.6 will be a stable channel. Any additional release for the v0.6 version
will only contain bug fixes. For instance, upgrading v0.6.1000 to v0.6.1002
should fix bugs, but not introduce features.

The stable channel will be available at Google Play as regular app updates.
For F-Droid, it is expected that this will also be the main branch, although
this paragraph will change once I actually understand how to deploy to F-Droid.
For PC and web, this will be the versions available through itch.io.

After releasing v0.6, the branch will be immediately forked into v0.7. v0.7
will be a development branch for the next kanban column. New features and
changes will be added here as work on the kanban column continues.

The development channel contains snapshot versions of the development branch.
The development channel will be available at Google Play via the open beta
program. If a user joins the open beta program, the app will eventually
update from v0.6 to v0.7 and will receive work in progress for these new
features. Development releases will also be available via GitHub as pre-release
versions and maybe in itch.io or the game website for PC and web.

Once work for the development branch is stabilized and all the planned features
are merged and tested, the version will be bumped to v0.8, and v0.8 will be the
new stable channel, containing releases that have all the new features made
during v0.7. Then it will be immediately forked to v0.9 to work on the next
column (v0.10) and so on.

At the moment of writing, it is expected that v0.10 will contain all the desired
features on a "complete" version of the game, so it is expected that v0.10
will actually be released as v1.0 (and then immediately forked as v1.1, to plan
for new features for v1.2).

## Build numbers are global

Build numbers increment over time as new commits are processed by the CI. Double
push means double build number increment. As said above, it is acceptable that
two versions in the same channel have build numbers separated by more than one
number, such as v0.6.390 being followed by v0.6.397.

Additionally, since the CI is shared by both channels, it is possible that a
release in the stable channel has a higher build number than a release in the
development channel, if it was done later in time. For instance, over time
the CI server may build v0.6.400, v0.7.401, v0.6.402 and v0.7.403.

This is intentional. Android requires applications to have a monotonically
incremented build number. I wouldn't be able to fork the build number scheme
so that each branch bumps on its own (v0.6.518, v0.7.520, v0.6.519), because
then Google Play would not allow me to upload the v0.6.519 version to the
production release, since there is already a version numbered 520 in the
open beta program.

## Rationale

Semantic releases increment major, minor or patch versions depending on features
for the API, and while this is a good KPI for a developer, it doesn't usually
reflect well on the user side.

For instance, making a big refactor that changes classes and removes methods
is a breaking change that under semantic versioning causes at least the minor
version to bump, most times also the major version. However, if these changes
only affect API and internal code but do not have an effect on what the user
sees when the app is opened, is it really a major version?

Using a family version ("v0.5", "v0.6", "v0.8") and a build version is a more
simple approach:

- The version immediately communicates the feature set present in the game.
  For instance, version v0.8 has features that were not present in version
  v0.6.

- The odd/even thing immediatelly communicates whether the user is playing a
  snapshot or a stable release, without having to introduce alphabetical tags
  to the version code such as "v0.6-dev".

When combined with the build number as the third number, I see the following:

- Handling support requests is easier (you can just ask for the third number
  the user sees in the main screen page).

- You don't have to think about whether the work you did today should bump
  which of the three numbers, because the decision is mostly already taken.
  You only bump the major or minor version at some specific and important
  moments in time.
