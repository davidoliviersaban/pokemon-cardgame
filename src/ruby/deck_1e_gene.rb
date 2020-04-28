require 'squib'

#deck = Squib.csv file: %w(generatedResources/pokemon1eGene.simplified.csv)
#infos = Squib.csv file: %w(generatedResources/pokemon1eGene.infos.csv)
#competences = Squib.csv file: %w(generatedResources/pokemon1eGene.competences.csv)
#resistances = Squib.csv file: %w(generatedResources/pokemon1eGene.resistances.csv)
deck = Squib.csv file: %w(generatedResources/pokemon8eGene.simplified.csv)
infos = Squib.csv file: %w(generatedResources/pokemon8eGene.infos.csv)
competences = Squib.csv file: %w(generatedResources/pokemon8eGene.competences.csv)
resistances = Squib.csv file: %w(generatedResources/pokemon8eGene.resistances.csv)

Squib::Deck.new(cards: deck["Name"].size,
                layout: %w(src/resources/Vlayout.yml src/resources/infos.yml src/resources/Vcards.yml src/resources/competences.yml src/resources/resistances.yml)) do
  rect layout: :bleed
#  rect layout: 'cut', stroke_color: :black # cut line as defined by TheGameCrafter
  cut_zone radius: 0.0,  stroke_color: :black
#  rect layout: :frame # safe zone as defined by TheGameCrafter
  rect layout: :frame, fill_color: :white
  rect layout: :inside
  safe_zone radius: 0.0, stroke_color: :red

  # deck = xlsx file: 'even-bigger.xlsx'
  text str:deck["Name"], layout: "Title"
  png file: infos["Image"].map{ |img| "pokemons/images/"+img}, layout: "Image"

  %w(PV Vitesse Attaque Defense Att_Spe Def_Spe Capture).each do |key|
    text str: key, layout: key+"Icon"
  end

  %w(PV Vitesse Attaque Defense Att_Spe Def_Spe Capture).each do |key|
    text str: deck[key], layout: key
  end

  %w(Competence Type Category Power Accuracy).each do |key|
    %w(1 2 3 4).each do |id|
      text str: competences[key+id], layout: key+id, stroke_color: :red
      rect layout:key+id
    end
  end

  fillcolor = Hash.new
  fillcolor["0"] = "#AAAAAA"
  fillcolor["1/4"] = "#AAAAFF"
  fillcolor["1/2"] = "#AAFFAA"
  fillcolor["1"] = "#AAFFFF"
  fillcolor["2"] = "#FFFFAA"
  fillcolor["4"] = "#FFAAAA"
  fillcolor[""] = "#000000"

  %w(Acier Combat Dragon Eau Electrik Fee Feu Glace Insecte Normal Plante Poison Psy Roche Sol Spectre Tenebres Vol).each do |key|
    rect layout: key#, fill_color: resistances[key].map{|c| fillcolor[c]}
    rect layout: key, y: 1010#, fill_color: resistances[key].map{|c| fillcolor[c]}
    text str: key[0..1].upcase, layout: key, background: resistances[key].map{|c| fillcolor[c]}
    text str: resistances[key], layout: key, y: 1010, color: :black
  end

  save_png prefix: deck["Name"]
end