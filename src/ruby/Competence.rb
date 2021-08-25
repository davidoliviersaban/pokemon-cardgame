require 'squib'

deck = Squib.xlsx file: 'src/resources/AllCompetences.xlsx'

Squib::Deck.new(cards: deck["Competence"].size,
                layout: %w(src/resources/VHalfCard.yml),
                height: "1.425in", width: "2.75in") do
#  rect layout: :bleed
#  rect layout: 'cut', stroke_color: :black # cut line as defined by TheGameCrafter
#  rect layout: :frame # safe zone as defined by TheGameCrafter
  rect layout: :frame, fill_color: :white
  rect layout: :inside

  category = Hash.new
  %w(Physique Special Statut).each do |key|
    category[key] = "pokemons/icons/"+key+".png"
  end

  # deck = xlsx file: 'even-bigger.xlsx'
  png file: deck["CType"].map{ |img| "pokemons/icons/"+img+".png"}, layout: "CType"
  png file: deck["Category"].map{ |img| category[img]}, layout: "Category"
#    text str: deck["Dice"+id], layout: "Dice"+id, valign: :middle
  %w(Competence).each do |key|
    rect layout:key
    text str: deck[key], layout: "CTitle", stroke_color: :red
    text str: deck["Description"], layout: "Comments"
  end
  %w(Power Accuracy).each do |key|
    rect layout:key
    text str: deck[key], layout: key, stroke_color: :red
    png layout: key+"Icon"
  end

  save_png suffix: deck["Competence"].map{|c| "Competence_"+c }
end