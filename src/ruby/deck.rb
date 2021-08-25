require 'squib'

deck = Squib.xlsx file: 'src/resources/AllPokemons.xlsx'

Squib::Deck.new(cards: deck["Name"].size,#cards: deck["Name"].size, # cards: 1,#
                layout: %w(src/resources/Vlayout.yml src/resources/infos.yml src/resources/Vcards.yml src/resources/competences.yml src/resources/resistances.yml)) do

  fillcolor = Hash.new
  fillcolor[0] = "#AAAAAA"
  fillcolor["0"] = "#AAAAAA"
  fillcolor["1/4"] = "#AAAAFF"
  fillcolor["1/2"] = "#AAFFAA"
  fillcolor[1] = "#AAFFFF"
  fillcolor["1"] = "#AAFFFF"
  fillcolor[2] = "#FFFFAA"
  fillcolor["2"] = "#FFFFAA"
  fillcolor[4] = "#FFAAAA"
  fillcolor["4"] = "#FFAAAA"
  fillcolor[9] = "#FFAAAA"
  fillcolor["9"] = "#FFAAAA"

  

  rect layout: :bleed
  cut_zone radius: 0.0,  stroke_color: :black
  rect layout: :frame, fill_color: deck["EvolutionId"].map { |id| fillcolor[id] }
  rect layout: :inside, fill_color: :white

# Data Generation
  evolutionText = []
  deck["EvolutionDescription"].zip(deck["Evolution"]).map do |a,b|
    if a != nil
      evolutionText << a+" "+b
    else
      evolutionText << ""
    end
  end


  category = Hash.new
  %w(Physique Special Statut).each do |key|
    category[key] = "src/resources/icons/"+key+".png"
  end

  png layout: "Icon", file: deck["Type1"].map{ |img| "src/resources/icons/"+img+".png"}, x: 77, y: 80
  png layout: "Icon", file: deck["Type2"].map{ |img| "src/resources/icons/"+img+".png"}, x: 77, y: 120
  png file: deck["Image"].map{ |img| "src/resources/images/"+img}, layout: "Image"

  text str: deck["Name"], layout: "Title"
  text str: evolutionText, layout: "Evolution"


  %w(Attaque Defense Att_Spe Def_Spe).each do |key|
    png layout: key+"Icon"
  end
  %w(PV Vitesse Attaque Defense Att_Spe Def_Spe Capture).each do |key|
    text str: key, layout: key+"Text"
    text str: deck[key], layout: key
  end


  %w(1 2).each do |id|
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

  %w(1 2).each do |id|
    png file: deck["Type"+id].map{ |img| "src/resources/icons/"+img+".png"}, width: 250, height: :scale, y: 870, x:120+(id.to_i-1)*250
  end

  %w(Acier Combat Dragon Eau Electrik Fee Feu Glace Insecte Normal Plante Poison Psy Roche Sol Spectre Tenebres Vol).each do |key|
    rect layout: key, y: 1010, fill_color:  deck[key].map{|c| fillcolor[c]}
    text str: deck[key], layout: key, y: 1010, color: :black
    png layout: key+"Icon"
  end

  save_png prefix: deck["Id"].zip(deck["Name"]).map{|a,b| "#"+a.to_s.rjust(3,"0")+"_"+b}

end