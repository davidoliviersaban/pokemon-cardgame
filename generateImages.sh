#!/usr/bin/env bash
#rm -rf _cards* _board _output imagesToPrint
#ruby src/ruby/deck.rb
cd ../printableCardsAppender
./gradlew appendCard --args="../pokemon-cardgame/_output ../pokemon-cardgame/imagesToPrint/board A4 true"
#./gradlew run --args="../windwalkers-cardgame/_cards2   ../windwalkers-cardgame/imagesToPrint/cards_v2_ A4 false"