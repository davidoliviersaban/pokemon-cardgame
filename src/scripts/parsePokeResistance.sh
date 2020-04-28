perl -p -e 's,^ *,,g'  resistances.html   |
    egrep '^<td|^<tr>' |
    perl -p -e  's,<td>.*title=",,g'  |
    perl -p -e 's, \(type\)"><img.*,\,,g' |
    perl -p -e 's,<td class="standard">,<td class="standard">× 1,g' |
    perl -p -e 's,1/2,0.5,g' |
    perl -p -e 's,1/4,0.25,g' |
    perl -p -e 's,<td class.*× (\d\.?\d*),$1\,,g' |
    perl -p -e 's,\n,,g' |
    perl -p -e 's,<.?tr>,\n,g' |
    perl -p -e 's/Acier\,Combat/Type Defensif\,Acier\,Combat/' |
    perl -p -e 's,É,E,g' |
    perl -p -e 's,é|è,e,g'
