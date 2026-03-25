The Open Open League Simulator. WIP.

### Workflow:

#### This elo simulator takes a set of clubs and their current elos as well as a set of matches and calculates the resulting elos.

#### The simulator exports a text file of the final elos and a csv file of up to 100 most recent elo values.

The input and output club elos file will be of the same format. 
The simulator may optionally take historical value input in the same .csv format as the historical value output.
The cmdline switch `-e` (for extension) allows the next match file input to take starting elo and historical elo from the previous file's output.
(i.e. it sets the input files to the output files of the most recent execution)

The simulator can also be used directly on Discord chat exports. 
Use the parse tag to prase files into match format, and use the aggregate tag to combine matches from leagues 1, 2, and 3 over the same time period.

### Input Format:

Club File:
```
Name Elo GamesPlayed
```

Match File:
```
MatchType 
HomeTeam Score : Score AwayTeam
```
Multiple matchtypes may be specified in a single match file by restating the matchtype before the relevant scoreline.

History File:
```
Name,EloDay1,EloDay2,...
```
If the history file has more than 100 historical elo values, the earliest ones will be dropped in the output of the 100 most recent values.
### Command Line Instructions:
All instructions are optional.

`-m`: match input file
`-c`: club input file
`-o`: club output file

`-h`: input history csv (defaults to `NULL`)

`-p`: parsing required on match input file

`-a`: aggregation required on match input file, requires 3 arguments strictly

`-e`: extension, set previous elo & history outputs to input