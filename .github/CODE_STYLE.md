# Java Style
For the most part, Pathfinder follows Google's Java specifications. There are
some notable exceptions, however.
- Lines are limited to 80 characters in length, regardless of the situation.
  No line should be over 80 characters in length.
- Spacing is more than welcome, always.
- All comments should have one line of space between the comment and any above
  code.

# Documentation
Documentation for Pathfinder is largely maintained through JavaDoc comments
in source files. There are some standards which apply to these comments:
- Any file you contribute to in a meaningful way should have an `@author` tag
  with your name.
- All files should have an `@version` tag. This tag represents the file's
  version. Any time the file is modified, this number should be incremented.
  - "Patch" version number should be increased for minor changes. These
    include spelling mistakes, inaccuracy mistakes, etc.
  - "Minor" version numbers should be increased for any changes to the class.
    These changes include refactoring, majorly changing the functionality of
    a method, etc.
  - "Major" version numbers should be reserved for when the file is completely
    overhauled.
  - All files should start on version "1.0.0" and should be incremented during
    any changes. If the change changes the functionality of the class in a
    way that the existing documentation is invalidated, this documentation
    should be updated when the file is changed.
- All files should have an `@since` tag. This tag represents the point in time
  at which the file was added to the source files. Files with a since tag
  indicating that they've been around since 0.1.0 are original files - as in
  files that were included originally in the project's release.
  - This since tag should not ever be updated.
  - Since tags should represent the project's latest minor or pre-release at
    the time of the file's addition.