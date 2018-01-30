-- auteur :  Leprêtre Guillaume et Caroni Christopher

module Tortue_volante where

import Graphics.Gloss
import LSysteme


type EtatTortue = (Point, Float)

-- l’état de la tortue et le chemin qu’elle a parcouru jusqu’à maintenant
type EtatDessin = ([EtatTortue], [Path])

type Config = (EtatTortue -- État initial de la tortue
              ,Float      -- Longueur initiale d’un pas
              ,Float      -- Facteur d’échelle
              ,Float      -- Angle pour les rotations de la tortue
              ,[Symbole]) -- Liste des symboles compris par la tortue


etatInitial :: Config -> EtatTortue
etatInitial (x,_,_,_,_) = x

longueurPas :: Config -> Float
longueurPas (_,x,_,_,_) = x

facteurEchelle :: Config -> Float
facteurEchelle (_,_,x,_,_) = x

angle :: Config -> Float
angle (_,_,_,x,_) = x

symbolesTortue :: Config -> [Symbole]
symbolesTortue (_,_,_,_,x) = x

avance :: Config -> EtatTortue -> EtatTortue
avance conf ((x,y),a) = ((x',y'),a)
  where x' = x + (longueurPas conf) * (cos a)
        y' = y + (longueurPas conf) * (sin a)

tourneAGauche :: Config -> EtatTortue -> EtatTortue
tourneAGauche conf (point, cap) = (point, cap')
  where cap' = cap + (angle conf)

tourneADroite :: Config -> EtatTortue -> EtatTortue
tourneADroite conf (point, cap) = (point, cap')
    where cap' = cap - (angle conf)

-- supprime tous les symboles qui ne sont pas des ordres pour la tortue dans le mot passé en argument
filtreSymbolesTortue :: Config -> Mot -> Mot
filtreSymbolesTortue c m = [s | s <- m, s `elem` symbolesTortue c]

-- Nouvelle fonction
interpreteSymbole :: Config -> EtatDessin -> Symbole -> EtatDessin
interpreteSymbole c (et:ets,p:ps) s | s == '['  = (et:et:ets, p:p:ps)
                                    | s == ']'  = (ets, p:ps)
                                    | otherwise = (et':ets, (p ++ [fst et']):ps)
                                                where et' | s == 'F'  = avance c et
                                                          | s == '+'  = tourneAGauche c et
                                                          | s == '-'  = tourneADroite c et
                                                          | otherwise = error "wrong symbol"


interpreteMot :: Config -> Mot -> Picture
interpreteMot c m = line (head (snd (foldl (interpreteSymbole c) iE mF)))
   where iE = ([etatInitial c], [[fst (etatInitial c)]])
         mF = filtreSymbolesTortue c m

-- pour un instant, calcule l’image correspondant
lsystemeAnime :: LSysteme -> Config -> Float -> Picture
lsystemeAnime lsys conf instant = interpreteMot newConf (lsys !! enieme)
  where enieme = round instant `mod` 8
        newConf = case conf of
          (etat, pas, echelle, angle, syms) -> (etat, pas * (echelle ^ enieme), echelle, angle, syms)

-- Orig von koch
dessin :: Picture
dessin = interpreteMot (((-150,0),0),100,1,pi/3,"F+-") "F+F--F+F"

--EXEMPLES

vonKoch1 :: LSysteme
vonKoch1 = lsysteme "F" regles
    where regles 'F' = "F-F++F-F"
          regles  s  = [s]

vonKoch2 :: LSysteme
vonKoch2 = lsysteme "F++F++F++" regles
    where regles 'F' = "F-F++F-F"
          regles  s  = [s]

hilbert :: LSysteme
hilbert = lsysteme "X" regles
    where regles 'X' = "+YF-XFX-FY+"
          regles 'Y' = "-XF+YFY+FX-"
          regles  s  = [s]

dragon :: LSysteme
dragon = lsysteme "FX" regles
    where regles 'X' = "X+YF+"
          regles 'Y' = "-FX-Y"
          regles  s  = [s]

brindille :: LSysteme
brindille = lsysteme "F" regles
    where regles 'F' = "F[-F]F[+F]F"
          regles  s  = [s]

broussaille :: LSysteme
broussaille = lsysteme "F" regles
    where regles 'F' = "FF-[-F+F+F]+[+F-F-F]"
          regles  s  = [s]

brindilleAnime :: Float -> Picture
brindilleAnime = lsystemeAnime brindille (((0, -400), pi/2), 800, 1/3, 25*pi/180, "F+-[]")

broussailleAnime :: Float -> Picture
broussailleAnime = lsystemeAnime broussaille (((0, -400), pi/2), 500, 2/5, 25*pi/180, "F+-[]")

vonKoch1Anime :: Float -> Picture
vonKoch1Anime = lsystemeAnime vonKoch1 (((-400, 0), 0), 800, 1/3, pi/3, "F+-")

vonKoch2Anime :: Float -> Picture
vonKoch2Anime = lsystemeAnime vonKoch2 (((-400, -250), 0), 800, 1/3, pi/3, "F+-")

hilbertAnime :: Float -> Picture
hilbertAnime = lsystemeAnime hilbert (((-400, -400), 0), 800, 1/2, pi/2, "F+-")

dragonAnime :: Float -> Picture
dragonAnime = lsystemeAnime dragon (((0, 0), 0), 50, 1, pi/2, "F+-")


main :: IO ()
main = animate (InWindow "L-systeme" (1000, 1000) (0, 0)) white hilbertAnime
