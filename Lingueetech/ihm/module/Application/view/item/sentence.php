<?php 
    $id = 0; // Identifiant de la phrase
    $fr = "Exemple."; // Phrase en francais
    $tokens = array("This" => 0.6, "is" => 0.5, "an" => 0.8, "example." => 0.2);
    $difficulty = "hard"; // Difficulte [easy, normal, hard]
    $fav = true; // Temoin de favori
    $like = 5; // Nombre de "J'aime"
    $dislike = 2; // Nombre de "Je n'aime pas"
?>

<section id="sentence">

    <h2>
        <a href="/sentence">Phrase <?php echo $id; ?></a>
        <span class="label
        <?php
                if($difficulty == "easy") echo "label-success";
                else if($difficulty == "hard") echo "label-danger";
                else echo "label-default";
        ?> pull-right"
        >
            <?php
                if($difficulty == "easy") echo "Facile";
                else if($difficulty == "hard") echo "Difficile";
                else echo "Normal";
            ?>
        </span>
    </h2>
    <ul class="list-group">
        <?php if(!empty($fr)) { ?>
        <li class="list-group-item"><img src="img/fr.svg" class="flag img-rounded"> <?php echo $fr; ?></li>
        <?php } if(!empty($tokens)) { ?>
        <li class="list-group-item active">
            <img src="img/en.svg" class="flag img-rounded">
            <?php foreach($tokens as $token => $score) {?>
                <a data-score="<?php echo $score; ?>"><?php echo $token; ?></a>
            <?php } ?>
        </li>
        <?php } ?>
    </ul>
    <div class="fa-lg">
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
    <script type="text/javascript">
        $("#sentence ul.list-group a").css("color", function(index, value) {
            var green = parseInt(parseFloat($(this).attr("data-score")) * 255);
        	return green < 200 ? "rgb(255, " + green + ", 0)" : value;
        }).click(function(event) {
		});
    </script>
</section>