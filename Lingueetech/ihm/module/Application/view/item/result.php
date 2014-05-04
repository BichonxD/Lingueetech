<?php 
    $id = 0; // Identifiant de la phrase
    $fr = "Exemple."; // Phrase en francais
    $en = "Example."; // Phrase en anglais
    $difficulty = "hard"; // Difficulte [easy, normal, hard]
    $fav = true; // Temoin de favori
    $like = 5; // Nombre de "J'aime"
    $dislike = 2; // Nombre de "Je n'aime pas"
?>

<div class="panel
<?php
        if($difficulty == "easy") echo "easy panel-success";
        else if($difficulty == "hard") echo "hard panel-danger";
        else echo "normal panel-default";
?>
">
    <div class="panel-heading">
        <span class="label
        <?php
                if($difficulty == "easy") echo "label-success";
                else if($difficulty == "hard") echo "label-danger";
                else echo "label-default";
        ?> pull-right">
            <?php
                if($difficulty == "easy") echo "Facile";
                else if($difficulty == "hard") echo "Difficile";
                else echo "Normal";
            ?>
        </span>
        <h4><a href="/sentence">Phrase <?php echo $id; ?></a></h4>
    </div>
    <ul class="list-group">
        <?php if(!empty($fr)) { ?>
        <li class="list-group-item"><img src="img/fr.svg" class="flag img-rounded"><?php echo $fr; ?></li>
        <?php } if(!empty($en)) { ?>
        <li class="list-group-item"><img src="img/en.svg" class="flag img-rounded"><?php echo $en; ?></li>
        <?php } ?>
    </ul>
    <div class="panel-footer fa-lg">
        <div class="pull-right">
            <a title="J'aime"><i class="fa fa-thumbs-up"></i></a>
            <a title="Je n'aime pas"><i class="fa fa-thumbs-down"></i></a>
            <span class="badge"><?php echo $like - $dislike; ?></span>
        </div>
        <div>
            <a title="Ajouter &agrave; une liste"><i class="fa fa-plus-circle"></i></a>
            <a title="Ajouter aux favoris" <?php if($fav) echo "class='text-info'"?>><i class="fa fa-star"></i></a>
        </div>
    </div>
</div>