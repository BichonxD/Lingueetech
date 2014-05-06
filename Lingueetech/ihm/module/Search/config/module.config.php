<?php
return array(
    'controllers' => array(
        'invokables' => array(
            'Search\Controller\Search' => 'Search\Controller\SearchController',
        ),
    ),
    'router' => array(
        'routes' => array(
            'search' => array(
                'type'    => 'Segment',
                'options' => array(
                    // Change this to something specific to your module
                    'route'    => '/search',
                    'defaults' => array(
                        // Change this value to reflect the namespace in which
                        // the controllers for your module are found
                        '__NAMESPACE__' => 'Search\Controller',
                        'controller'    => 'Search',
                        'action'        => 'index',
                    ),
                ),
            ),
        ),
    ),
    'view_manager' => array(
        'template_path_stack' => array(
            'Search' => __DIR__ . '/../view',
        ),
    ),
);
