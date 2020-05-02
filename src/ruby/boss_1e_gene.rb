require 'squib'

deck = Squib.xlsx file: %w(src/resources/Boss.xlsx)

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
  text str:deck["Name"].zip(deck["BOSS"]).map{|c,b| b[0..4]+" "+c}, layout: "BossTitle"

  png layout: "Icon", file: deck["Type1"].map{ |img| "src/resources/icons/"+img+".png"}, x: 77, y: 80
  png layout: "Icon", file: deck["Type2"].map{ |img| "src/resources/icons/"+img+".png"}, x: 77, y: 120

#  print deck["Name"].zip(deck["Image"])
#
  png file: deck["Image"].map{ |img| "src/resources/images/"+img}, layout: "Image"

  %w(Attaque Defense Att_Spe Def_Spe).each do |key|
    png layout: key+"Icon"
  end
  %w(PV Vitesse Attaque Defense Att_Spe Def_Spe).each do |key|
    text str: key, layout: key+"Text"
    text str: deck[key], layout: key
  end


  category = Hash.new
  %w(Physique Special Statut).each do |key|
    category[key] = "src/resources/icons/"+key+".png"
  end

  %w(1 2 3 4).each do |id|
    png file: deck["CType"+id].map{ |img| "src/resources/icons/"+img+".png"}, layout: "CType"+id
    png file: deck["Category"+id].map{ |img| category[img]}, layout: "Category"+id
    text str: deck["Dice"+id], layout: "Dice"+id, valign: :middle
    %w(Competence).each do |key|
      rect layout:key+id
      text str: deck[key+id].map{|txt| txt.gsub("Empty","Competence")}, layout: "Title"+id, stroke_color: :red
      text str: deck[key+id+"Description"], layout: "Comments"+id
    end
    %w(Power Accuracy).each do |key|
      rect layout:key+id
      text str: deck[key+id], layout: key+id, stroke_color: :red
    end
  end

  fillcolor = Hash.new
  fillcolor[0] = "#AAAAAA"
  fillcolor["0"] = "#AAAAAA"
  fillcolor["1/4"] = "#AAAAFF"
  fillcolor["1/2"] = "#AAFFAA"
  fillcolor[1] = "#AAFFFF"
  fillcolor[2] = "#FFFFAA"
  fillcolor[4] = "#FFAAAA"
  fillcolor["1"] = "#AAFFFF"
  fillcolor["2"] = "#FFFFAA"
  fillcolor["4"] = "#FFAAAA"

  %w(Acier Combat Dragon Eau Electrik Fee Feu Glace Insecte Normal Plante Poison Psy Roche Sol Spectre Tenebres Vol).each do |key|
    #rect layout: key, fill_color:  deck[key].map{|c| fillcolor[c]}
    #text str:  deck[key].map{|c| typeAcronym[c]}, layout: key, background: deck[key].map{|c| fillcolor[c]}
    rect layout: key, y: 1010, fill_color:  deck[key].map{|c| fillcolor[c]}
    text str: deck[key], layout: key, y: 1010, color: :black
    png layout: key+"Icon"
  end


  save_png prefix: deck["BOSS"]+deck["Name"], dir: "_boss"
end