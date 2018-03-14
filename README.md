# Formation_Spark_TextSearch
## Recherche de mot dans le livre _ALICE'S ADVENTURES IN WONDERLAND_

### Install
```sh
pip install -r requirements.txt
```
### Usage
```sh
./main.py <inputDirname>
```

### Filtrage
Les mots sont filtrés selon les critères suivant:
- Les caractères `,.;:?!"-'*` en début et en fin de mot sont supprimés.
- Les mots contenant les caractères @ ou / sont supprimés.
- Les mots sont mis en caractères minuscules .

### Affichage
- Le mot le plus long du text
- Le mot de quatre lettres le plus fréquent
- Le mot de quinze lettres le plus fréquent
