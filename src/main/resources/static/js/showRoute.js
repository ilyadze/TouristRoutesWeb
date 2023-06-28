var xhr = new XMLHttpRequest();
const path = window.location.pathname;
const id = path.substring(path.lastIndexOf('/') + 1);
console.log(id);

getPoints(id);
function getPoints(id) {

    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                var json = JSON.parse(xhr.responseText);
                coordinates = json.map(point => [point.latitude, point.longitude]);
                if(coordinates === 2) {
                    function init() {
                        var referencePoints = coordinates,
                            viaIndexes = [2];
                        console.log('ref---------');
                        console.log(referencePoints);

                        // var multiRoute = new ymaps.multiRouter.MultiRoute({
                        //     referencePoints: referencePoints,
                        //     params: {viaIndexes: viaIndexes}
                        // }
                        // Создаем модель мультимаршрута.
                        var multiRouteModel = new ymaps.multiRouter.MultiRouteModel(referencePoints, {
                                // Путевые точки можно перетаскивать.
                                // Маршрут при этом будет перестраиваться.
                                wayPointDraggable: false,
                                boundsAutoApply: true
                            }),

                            // Создаём выпадающий список для выбора типа маршрута.
                            routeTypeSelector = new ymaps.control.ListBox({
                                data: {
                                    content: 'Как добраться'
                                },
                                items: [
                                    new ymaps.control.ListBoxItem({data: {content: "Авто"},state: {selected: true}}),
                                    new ymaps.control.ListBoxItem({data: {content: "Общественным транспортом"}}),
                                    new ymaps.control.ListBoxItem({data: {content: "Пешком"}}),
                                    new ymaps.control.ListBoxItem({data: {content: "Велосипед"}})
                                ],
                                options: {
                                    itemSelectOnClick: false
                                }
                            }),
                            // Получаем прямые ссылки на пункты списка.
                            autoRouteItem = routeTypeSelector.get(0),
                            masstransitRouteItem = routeTypeSelector.get(1),
                            pedestrianRouteItem = routeTypeSelector.get(2),
                            bikeRoutePoint = routeTypeSelector.get(3);

                        // Подписываемся на события нажатия на пункты выпадающего списка.
                        autoRouteItem.events.add('click', function (e) { changeRoutingMode('auto', e.get('target')); });
                        masstransitRouteItem.events.add('click', function (e) { changeRoutingMode('masstransit', e.get('target')); });
                        pedestrianRouteItem.events.add('click', function (e) { changeRoutingMode('pedestrian', e.get('target')); });
                        bikeRoutePoint.events.add('click', function (e) { changeRoutingMode('bicycle', e.get('target')); });
                        ymaps.modules.require([
                            'MultiRouteCustomView'
                        ], function (MultiRouteCustomView) {
                            // Создаем экземпляр текстового отображения модели мультимаршрута.
                            // см. файл custom_view.js
                            new MultiRouteCustomView(multiRouteModel);
                        });

                        // var myCoords = [55.754952,37.615319];
                        // myMap.geoObjects.add(
                        //     new ymaps.Placemark(myCoords,
                        //         {iconContent: 'Где метро?'},
                        //         {preset: 'islands#greenStretchyIcon'}
                        //     )
                        // );

                        // Создаем карту с добавленной на нее кнопкой.
                        var myMap = new ymaps.Map('map', {
                                center: [53.90356363622448,27.562299975309177],
                                zoom: 7,
                                controls: [routeTypeSelector]
                            }, {
                                buttonMaxWidth: 300,
                                // restrictMapArea: [
                                //     [51.68920899288854,27.773234360554287],
                                //     [56.089621108656445,28.14779765636679]
                                // ]
                            }),

                            // Создаем на основе существующей модели мультимаршрут.
                            multiRoute = new ymaps.multiRouter.MultiRoute(multiRouteModel, {
                                // Путевые точки можно перетаскивать.
                                // Маршрут при этом будет перестраиваться.
                                wayPointDraggable: false,
                                boundsAutoApply: true
                            });

                        // Добавляем мультимаршрут на карту.
                        myMap.geoObjects.add(multiRoute);

                        function changeRoutingMode(routingMode, targetItem) {
                            multiRouteModel.setParams({ routingMode: routingMode }, true);

                            // Отменяем выбор элементов.
                            autoRouteItem.deselect();
                            masstransitRouteItem.deselect();
                            pedestrianRouteItem.deselect();
                            bikeRoutePoint.deselect();
                            // Выбираем элемент и закрываем список.
                            targetItem.select();
                            routeTypeSelector.collapse();
                        }
                    }
                } else {
                    function init () {
                    var referencePoints = coordinates,
                        viaIndexes = [2];
                    console.log('ref---------');
                    console.log(referencePoints);

                    // var multiRoute = new ymaps.multiRouter.MultiRoute({
                    //     referencePoints: referencePoints,
                    //     params: {viaIndexes: viaIndexes}
                    // }
                    // Создаем модель мультимаршрута.
                    var multiRouteModel = new ymaps.multiRouter.MultiRouteModel(referencePoints, {
                            // Путевые точки можно перетаскивать.
                            // Маршрут при этом будет перестраиваться.
                            wayPointDraggable: false,
                            boundsAutoApply: true
                        }),

                        // Создаём выпадающий список для выбора типа маршрута.
                        routeTypeSelector = new ymaps.control.ListBox({
                            data: {
                                content: 'Как добраться'
                            },
                            items: [
                                new ymaps.control.ListBoxItem({data: {content: "Авто"},state: {selected: true}}),
                                new ymaps.control.ListBoxItem({data: {content: "Общественным транспортом"}}),
                                new ymaps.control.ListBoxItem({data: {content: "Пешком"}}),
                                new ymaps.control.ListBoxItem({data: {content: "Велосипед"}})
                            ],
                            options: {
                                itemSelectOnClick: false
                            }
                        }),
                        // Получаем прямые ссылки на пункты списка.
                        autoRouteItem = routeTypeSelector.get(0),
                        masstransitRouteItem = routeTypeSelector.get(1),
                        pedestrianRouteItem = routeTypeSelector.get(2),
                        bikeRoutePoint = routeTypeSelector.get(3);

                    // Подписываемся на события нажатия на пункты выпадающего списка.
                    autoRouteItem.events.add('click', function (e) { changeRoutingMode('auto', e.get('target')); });
                    masstransitRouteItem.events.add('click', function (e) { changeRoutingMode('masstransit', e.get('target')); });
                    pedestrianRouteItem.events.add('click', function (e) { changeRoutingMode('pedestrian', e.get('target')); });
                    bikeRoutePoint.events.add('click', function (e) { changeRoutingMode('bicycle', e.get('target')); });
                    ymaps.modules.require([
                        'MultiRouteCustomView'
                    ], function (MultiRouteCustomView) {
                        // Создаем экземпляр текстового отображения модели мультимаршрута.
                        // см. файл custom_view.js
                        new MultiRouteCustomView(multiRouteModel);
                    });

                    // var myCoords = [55.754952,37.615319];
                    // myMap.geoObjects.add(
                    //     new ymaps.Placemark(myCoords,
                    //         {iconContent: 'Где метро?'},
                    //         {preset: 'islands#greenStretchyIcon'}
                    //     )
                    // );

                    // Создаем карту с добавленной на нее кнопкой.
                    var myMap = new ymaps.Map('map', {
                            center: [53.90356363622448,27.562299975309177],
                            zoom: 7,
                            controls: [routeTypeSelector]
                        }, {
                            buttonMaxWidth: 300,
                            // restrictMapArea: [
                            //     [51.68920899288854,27.773234360554287],
                            //     [56.089621108656445,28.14779765636679]
                            // ]
                        }),

                        // Создаем на основе существующей модели мультимаршрут.
                        multiRoute = new ymaps.multiRouter.MultiRoute(multiRouteModel, {
                            // Путевые точки можно перетаскивать.
                            // Маршрут при этом будет перестраиваться.
                            wayPointDraggable: false,
                            boundsAutoApply: true
                        });

                    // Добавляем мультимаршрут на карту.
                    myMap.geoObjects.add(multiRoute);

                    function changeRoutingMode(routingMode, targetItem) {
                        multiRouteModel.setParams({ routingMode: routingMode }, true);

                        // Отменяем выбор элементов.
                        autoRouteItem.deselect();
                        masstransitRouteItem.deselect();
                        pedestrianRouteItem.deselect();

                        // Выбираем элемент и закрываем список.
                        targetItem.select();
                        routeTypeSelector.collapse();
                    }



                }
                }
                ymaps.ready(init);
            }
        }
    }
    let username = "admin";
    let password = "admin";
    xhr.open("GET", "/points/" + id, true);
    xhr.setRequestHeader("Authorization", "Basic " + btoa(username + ":" + password));
    xhr.send();
}
