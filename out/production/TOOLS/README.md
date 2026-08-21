# The Open Open League Simulator

An Elo rating simulator for football leagues. WIP.

It takes a set of clubs with their current Elo ratings and a set of match
results, calculates the resulting Elo ratings, and exports the updated
ratings plus a history of the most recent Elo values.

## How it works

For each match, the simulator:

1. Computes the expected result of the home team:
   `E = 1 / (1 + 10^((awayElo - homeElo) / 400))`
   - League matches add a home advantage of **+30** to the home Elo.
   - Cup and friendly matches have no home advantage.
2. Selects a weight (K-factor) by match type:
   - `L` (league): **30**
   - `C` (cup): **25**
   - `F` (friendly): **5**
3. If the match is not a draw, the weight is scaled by the goal margin:
   `K = K * (1 + 0.1 * ln(|homeScore - awayScore|))`
4. Determines the actual result:
   - home win = `1.0`
   - draw = `0.5`
   - away win = `0.0`
5. Applies the Elo change:
   `delta = K * (result - E)`
   The home team gains `delta`, the away team loses `delta`.

Each club tracks a history of its most recent **100** Elo values; older
values are dropped automatically.

## Building and running

Compile (requires a JDK 9+ because the code uses string concatenation
invokedynamic):

```
javac *.java
```

Run with defaults:

```
java Main
```

With no arguments the simulator uses `clubs.txt` as club input,
`matches.txt` as match input, and writes `export.txt` (clubs) and
`history.csv` (history).

## Command-line arguments

```
java Main [options]
```

| Flag | Takes a value? | Description |
|------|----------------|-------------|
| `-c` | 1 argument | Club input file (default: `clubs.txt`) |
| `-h` | 1 argument | History input CSV (default: none; `history.csv` is then only the output target) |
| `-m` | 1 argument | Match input file (default: `matches.txt`) |
| `-o` | 1 argument | Club output file (default: `export.txt`) |
| `-p` | no | Parse the match input as a Discord chat export before simulating |
| `-a` | exactly 3 arguments | Aggregate three match files (league 1, 2, 3) into `aggregatedMatch.txt` |
| `-e` | no | Extension: reuse the previous run's output as input (`export.txt` as clubs, `history.csv` as history) |

### Argument ordering rules

Arguments are read from left to right, so:

1. **Every value-taking flag must be immediately followed by its value(s).**
   The value is always the very next argument(s), before any other flag.
2. **`-a` must be immediately followed by exactly 3 file paths**, in the
   order: league 1, league 2, league 3.
3. **`-p` and `-e` are switches** — they take no value and must not be
   followed by one.
4. **If two flags set the same value, the one that appears later wins.**
   For example `-c clubs.txt -e` ends with `export.txt`, while
   `-e -c clubs.txt` ends with `clubs.txt`.

A canonical order that matches the way the flags are grouped:

```
java Main -c <clubs> -h <history> -m <matches> -o <output> -p -a <l1> <l2> <l3> -e
```

Only the flags you need have to be present.

### Examples

Basic run using all defaults:

```
java Main
```

Explicit files:

```
java Main -c clubs.txt -h history.csv -m matches.txt -o export.txt
```

Parse a raw Discord export into match format, then simulate:

```
java Main -c clubs.txt -m e15l1.txt -p -o export.txt
```

Parse and aggregate three leagues over the same period, then simulate:

```
java Main -c eur15clubs.txt -p -a e15l1.txt e15l2.txt e15l3.txt -o export.txt
```

Continue from the previous run's output (extension):

```
java Main -e
```

## Processing pipeline

No matter the order of the flags on the command line, the simulation always
runs these steps in this order:

1. **Aggregate** (`-a`): if `-p` is also set, each of the three files is
   parsed first; then all three are merged into `aggregatedMatch.txt`, which
   becomes the match input.
2. **Parse** (`-p`): the match input is parsed as a Discord export, producing
   `parsed_<name>`.
3. **Load clubs** from the club input file.
4. **Load history** (optional) from the history CSV, replacing each club's
   stored Elo history.
5. **Load matches** and play them all, updating Elo ratings.
6. **Write output**: clubs are sorted by Elo (highest first) and written to
   the club output file and the history CSV.

## Input formats

Club file — one club per line:

```
Name Elo GamesPlayed
```

Match file — a match-type line followed by one or more scorelines:

```
MatchType
HomeTeam Score : Score AwayTeam
```

Multiple match types can be used in one file by restating the match type
before the relevant scorelines. The match types are:

- `L` — league match
- `C` — cup match
- `F` — friendly match

History file — CSV with a name and one Elo value per day:

```
Name,EloDay1,EloDay2,...
```

## Output formats

The club output file uses the same `Name Elo GamesPlayed` format as the club
input file.

The history output is CSV: `Name,Elo1,Elo2,...` containing up to the 100 most
recent Elo values per club (earliest values are dropped when the limit is
exceeded).

## Helper tools

- **`ChatParser`** converts raw Discord chat exports into match format,
  writing `parsed_<original filename>`.
- **`MatchAggregator`** combines match files for leagues 1, 2, and 3 over the
  same time period, writing `aggregatedMatch.txt`.
