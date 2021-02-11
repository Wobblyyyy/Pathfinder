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
- Virtualized Euclidean geometry and collision detection and prevention to
  navigate robots around a pre-mapped field.
- Support for different types of drivetrains, including swerve drive, tank
  drive, meccanum drive, and potentially even more.
- Several pathfinding algorithms and finders, including ThetaStar and AStar
  pathfinders.
- Trajectory generation for different types of drivetrains, such as swerve,
  tank, and meccanum, that allows you to calculate the fastest possible route
  to a given point.
- Abstract and general codebase that can be implemented just about anywhere
  with a JVM.
  
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
  
## Modules
If you've noticed, this project has three sub-modules:
- Pathfinder-frc
- Pathfinder-ftc
- Pathfinder-rlib

Each of these has a different artifact that's available/released for different
situations. For example, if you're writing code for an FRC challenge, you
should use the Pathfinder-frc release, not the Pathfinder or the
Pathfinder-ftc release. Pathfinder-rlib is the only one that doesn't seem to
make much sense, however, I'd like to say - it's for a future library I'm
working on designed to simplify the process of coding FRC and FTC robots.
