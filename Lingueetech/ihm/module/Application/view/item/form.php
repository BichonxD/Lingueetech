<section id="form">
    <h2>Recherche</h2>
    <div class="row">
        <div class="col-sm-8">
            <div class="input-group">
                <input type="text" placeholder="Expression &agrave; rechercher..." class="form-control">
                <div class="input-group-btn">
                    <button class="btn btn-primary" tabindex="-1" type="button"><i class="fa fa-search"></i></button>
                    <button class="btn btn-default dropdown-toggle" tabindex="-1" data-toggle="dropdown" type="button">Fran&ccedil;ais</button>
                    <ul class="dropdown-menu pull-right">
                        <li><a href="#">Automatique</a></li>
                        <li class="divider"></li>
                        <li><a href="#">Fran&ccedil;ais</a></li>
                        <li><a href="#">Anglais</a></li>
                    </ul>
                </div>
            </div>
        </div>
        <h5 class="col-sm-1 text-center">Ou</h5>
        <div class="col-sm-3">
            <button class="btn btn-primary btn-block" tabindex="-1" type="button"><i class="fa fa-random"></i> Trouver !</button>
        </div>
    </div>
    <script type="text/javascript">
    $("#form .dropdown-menu a").click(function() {
        $("#form .dropdown-toggle").html($(this).html());
    });
    </script>
</section>

<hr>