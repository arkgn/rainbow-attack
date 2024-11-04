# RainbowAttack
This project was developed for S-INFO-044 at Umons.

The goal of this homework is to implement a rainbow attack on password hash (sha256).

## Requirements
You have to install at least java 17 to run the jar file.

1. ***./gradlew installDist***
2. ***cd /build/install/RainbowAttck/bin***.
3. Put the hash file (.txt) that you want to attack in that folder. 

# Usage
**For Unix:**

You can run the following command to generate a rainbow table:

***./RainbowAttck --build -t [tableSize] -c [chainSize] -l [passwordSize]***

Ex: ***./RainbowAttck --build -t 1000000 -c 2000 -l 6*** (this will create a rainbow table for password size 6, with 1 million line and 2000 chain length for each)

And to attack given the hash file:

***./RainbowAttck --attack -h [hashFile.txt]***

Ex: ***./RainbowAttck --attack -h hash.txt***

**Note:** for windows you can use RainbowAttck.bat in the same folder.