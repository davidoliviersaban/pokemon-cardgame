perl -p -e 's,\n,,g' pokelist.html |
  perl -p -e 's,.*<tbody >(.*)</tbody>.*,$1,g' |
  perl -p -e 's,</tr>,\n,g' |
  perl -p -e 's,.*<tr >(.*),$1,g'