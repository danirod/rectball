# Dependency updating

## How often?

Surprisingly, not too often. Gradle has a compatibility matrix, so you cannot
update Java the day a new JDK is released, because it will probably not work
with the current Gradle version.

Also, keep in mind that this is a game, so it is difficult to write automatic
tests, and I cannot be the entire day checking that new versions of the game
libraries I am using keep working.

Dependency upgrade should be a process that is intentionally done when it makes
sense and not something that is done just weekly or monthly to mark a checkbox
or to say "hey, I don't use stale dependencies".

## Where is Dependabot?

Dependabot was removed because it just doesn't align with this policy.
