# RainbowAttack
This project was developed for S-INFO-044 at Umons.

The goal of this homework is to implement a rainbow attack on password hash (sha256).

## Requirements
You have to install at least java 17 to run the jar file.

```console
1. ./gradlew installDist
2. cd /build/install/RainbowAttck/bin
3. cp /path/of/hash/txtfile/that/you/want/to/attack .
```

# Usage
**For Unix:**

You can run the following command to generate a rainbow table:

```console
./RainbowAttck --build -t [tableSize] -c [chainSize] -l [passwordSize]
```

And to attack given the hash file:
```console
./RainbowAttck --attack -h [hashFile.txt]
```

**Note:** for windows you can use RainbowAttck.bat in the same folder.