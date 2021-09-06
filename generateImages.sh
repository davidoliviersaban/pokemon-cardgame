#!/usr/bin/env bash
rm -rf _cards* _board _output _boss
ruby src/ruby/deck.rb
ruby src/ruby/boss_1e_gene.rb
cd ../printableCardsAppender
./gradlew appendCard --args="../pokemon-cardgame/_output ../pokemon-cardgame/imagesToPrint/cards A4 false"
./gradlew appendCard --args="../pokemon-cardgame/_boss ../pokemon-cardgame/imagesToPrint/boss A4 false"