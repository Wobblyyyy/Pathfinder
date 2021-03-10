![Pathfinder Logo](logo.png)

![GPL-3.0](https://img.shields.io/github/license/Wobblyyyy/Pathfinder)
![Top Language](https://img.shields.io/github/languages/top/wobblyyyy/Pathfinder)
![Issues](https://img.shields.io/github/issues/Wobblyyyy/Pathfinder)
![Forks](https://img.shields.io/github/forks/Wobblyyyy/Pathfinder)
![Stars](https://img.shields.io/github/stars/Wobblyyyy/Pathfinder)
![Latest Release (Bleeding)](https://img.shields.io/github/v/release/wobblyyyy/Pathfinder?include_prereleases)
![Latest Release](https://img.shields.io/github/v/release/wobblyyyy/Pathfinder)

Odometry-dependent library for robotics-based competitions and challenges.
Click right [here](https://wobblyyyy.github.io/docs/pathfinder/quickstart.html) 
to get to the online documentation!

## Features
- Fully virtualized Euclidian geometry, allowing for collision detection, prevention, and
  advanced robot navigation. Create circles, rectangles, or circular rectangles (...?)
  and make your robot avoid them.
- Out-of-box support for different drivetrains, including swerve drive, meccanum drive, 
  tank drive, and any other form of holonomic drivetrain. Unsupported drivetrains can
  be added manually and will work exactly as intended.
- Efficient and optimized path finding and generation. Pathfinding planes in which there
  are no obstacles make use of accelerated path planning, allowing for on-the-fly path
  generation and following. As in: tell your robot to follow a path during Tele-Op, and
  it will do exactly that - no delay, no lag - just driving.
- A variety of different path following systems, including two, three, and four PID
  controller based path following systems.
- Plan trajectories using splines. Save them locally, record robot movement, and play
  back robot paths and trajectories. Optimize your drivetrain as effectively as possible,
  allowing for the fastest path following of your life.
- Abstract and general codebase that can be used anywhere with a JVM. Officially supported
  environments include...
  - FIRST Robotics Competition (on roboRIO)
  - FIRST Tech Challenge (on Android phones / control hub)
- Officially endorsed by NASA! Not really. Please don't sue me, actually. But it's cool,
  right? Very cool.
  
## Quickstart
Please see the online quickstart, available [here](https://wobblyyyy.github.io/docs/pathfinder/quickstart.html).

## Using Pathfinder to its Maximum Potential
Pathfinder is a rather expansive library designed for a wide variety of pathfinding situations.
As a result, it can be pretty challenging to document every single feature and option contained
in the library. It's a lot easier to provide guides on how to make use of specific features
in video form, as they take a lot less time to create and are generally easier to understand.
Please see [this link](https://wobblyyyy.github.io/docs/pathfinder/videos.html) to learn more.

## Issues and Bugs
If you notice an issue or bug with Pathfinder, please report it on the issue tracker and send me
an email. It's more important that you submit an issue report so the issue is documented
in a public fashion. Issues or bugs will be resolved as quickly as possible whenever possible. 

## Contributing to Pathfinder
Pathfinder is a fairly large project, and, as such, we always welcome contributors. If you notice
any bugs with Pathfinder, or would like to add any functionality, feel free to submit a pull
request. You can always feel free to add new features that don't detract from Pathfinder's
functionality. Examples include:
- New drivetrains (add drivetrains that aren't already supported)
- New following systems (new way to follow paths)
- New path generation system alternatives (new generator classes)

## Building
Pathfinder uses Gradle as a build system. Because Pathfinder is a library, the only task
you'll really need to run is `jar` or `shadowJar`. `shadowJar` will produce a jar with all
of the required dependencies bundled inside. This jar is about 300kb and includes all of
the libraries Pathfinder needs to operate. `jar` produces a jar much closer to 100kb in size.
It doesn't include any of the libraries Pathfinder needs to function - you'll need to add them
manually.

## Dependencies
- [OdometryCore](https://github.com/tmthecoder/OdometryCore)
- [EDT](https://github.com/Wobblyyyy/edt)
- [intra_utils](https://github.com/Wobblyyyy/intra_utils)
- [Pathfinder-Java](https://github.com/JaciBrunning/Pathfinder)
- [PathfindingCore](https://github.com/Wobblyyyy/PathfindingCore)
  
## Documentation
Pathfinder strives to be a well-documented project - every method, class,
field, interface, enum, etc - should all have documentation. Documentation, in
its current state, is available from either of these two places.
- JavaDoc [(here)](https://wobblyyyy.github.io/JavaDocs/Pathfinder/)
- Regular docs [(here)](https://wobblyyyy.github.io/docs/pathfinder/quickstart.html)
- Video docs [(here)](https://wobblyyyy.github.io/docs/pathfinder/videos.html)

If you notice any documentation issues, or are struggling to understand the
meaning of any documentation, please contact me (Colin) (wobblyyyy@gmail.com)
and I'll try to get any issues sorted out as quickly as possible.
