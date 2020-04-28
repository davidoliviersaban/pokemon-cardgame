mkdir -p pokemons/images
IMGS=$(perl -ne 'm,src="/images/thumb/([^".]+-[A-Z]+.png)(/250px-[^"]+\.png)?" decoding, && print "https://www.pokepedia.fr/images/$1\n";'   pokemons/* | grep -v 'SSB')
cd pokemons/images
for img in $IMGS; do
  # echo Downloading $img
  # Don't download file if already downloaded
  wget -nc $img
done