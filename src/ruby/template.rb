require 'squib'

Squib::Deck.new(cards: 1,
                layout: %w(src/resources/Vlayout.yml src/resources/infos.yml src/resources/Vcards.yml src/resources/competences.yml src/resources/resistances.yml)) do
                #                width: "3.75in", height: "2.75in") do
  rect layout: :bleed
#  rect layout: 'cut', stroke_color: :black # cut line as defined by TheGameCrafter
  cut_zone radius: 0.0,  stroke_color: :black
#  rect layout: :frame # safe zone as defined by TheGameCrafter
  rect layout: :frame, fill_color: :white
  rect layout: :inside
  safe_zone radius: 0.0, stroke_color: :red

  # deck = xlsx file: 'even-bigger.xlsx'
  text str: "____________", layout: "Title"
#  rect layout: "Image"
  rect layout: :Type1
  rect layout: :Type2

  %w(PV Vitesse Attaque Defense Att_Spe Def_Spe Capture).each do |key|
#    rect layout: key+"Icon"
    text str: key, layout: key+"Icon"
  end

  %w(PV Vitesse Attaque Defense Att_Spe Def_Spe Capture).each do |key|
#    rect layout: key
    text str: "__", layout: key
  end



  %w(Competence Power Accuracy).each do |key|
    #text str: key+"1", layout: key+"1", stroke_color: :red, height: 300
    rect layout:key+"1", height: 300
  end

  #  rect layout: "CompetenceNormal", y: :y+50
  # rect layout: "CompetenceEau", y: :y+100
  # rect layout: "CompetenceEau", y: :y+150
  
#  %w(Acier Combat Dragon Eau Electrik Fee Feu Glace Insecte Normal Plante Poison Psy Roche Sol Spectre Tenebres Vol).each do |key|
#    rect layout: key
#    text str: key[0..1].upcase, layout: key
#    text str: "x1", layout: key, y: 1006
#  end

  save_png prefix: "_template"
end