Files:

- abba-1: twelve one-byte integers that all fit in 7 bits
- abba-2: twelve two-byte integers that all fit in 7 bits
- abba-7-output: the result of running compaction on either
  + abba-1 with B=1 and bo=7, or
  + abba-2 with B=2 and bo=7
- tenbits-2: seven two-byte integers that all fit in 10 bits
- tenbits-10-output: result of running compaction on tenbits-2 with
  B=2 and bo=10
