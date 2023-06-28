const dom = {
    list: document.getElementById('point-view'),
}


var xhr = new XMLHttpRequest();
const path = window.location.pathname;
const id = path.substring(path.lastIndexOf('/') + 1);
var newPoints = [];

// let token = document.getElementById("csrf").value;
// // let nameToken = document.getElementById("csrf_name").value;
// xhr.setRequestHeader('X-CSRF-Token', token);

function getPoints(id) {

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                var json = JSON.parse(xhr.responseText);
                coordinates = json.map(point => [point.latitude, point.longitude]);
                // console.log(coordinates)
                var points = [];
                var selectElement = document.getElementById("category");
                var selectedValue = selectElement.value;
                if (coordinates.length === 2 && !(selectedValue==='None')) {
                    const [x1, y1] = coordinates[0];
                    const [x2, y2] = coordinates[1];
                    // console.log(coordinates);

                    // console.log('x1:', x1);
                    // console.log('y1:', y1);
                    // console.log('x2:', x2);
                    // console.log('y2:', y2);
                    // Получаем выбранную опцию
                    console.log(selectedValue + "----------------------")
                    var apiKey = '4f6d9c50-e543-4666-8734-47f93bb2268d';
                    let url = "https://search-maps.yandex.ru/v1/?text=%" + selectedValue + "&bbox=" + y2.toFixed(5)  + "," + x2.toFixed(5) + "~" + y1.toFixed(5)  + "," + x1.toFixed(5) + "&type=biz&rspn=1&results=5&lang=ru_RU&apikey=" + apiKey;
                    console.log(url);
                    xhr.onreadystatechange = function() {
                        if (xhr.readyState === 4) {
                            if (xhr.status === 200) {
                                dom.list.innerHTML = ``;
                                var json1 = JSON.parse(xhr.responseText);
                                // newPoints = json.map(point => [point.latitude, point.longitude]);
                                console.log(json1.features);
                                points.push(coordinates[0]);
                                let size = json1.features.length;
                                // console.log(newPoints);
                                for(let i =0 ;i < size;i++) {
                                    let el = json1.features[i].geometry.coordinates;
                                    points.push([el[1], el[0]]);
                                }
                                points.push(coordinates[1]);
                                console.log(points);
                                htmlList = `<h3 class="form-header">Точки маршрута</h3>`;

                                json1.features.map(el => {
                                    // points.push([el.geometry.coordinates[1], el.geometry.coordinates[0]]);
                                    newPoints.push({
                                        coordinates: [el.geometry.coordinates[1], el.geometry.coordinates[0]],
                                        name: el.properties.name,
                                        address: el.properties.description,
                                    });
                                    let htmlElement = `
                                             <div class="todo-task-container" style="border: 1px; background-color: #c6c6c6; border-radius: 5px; margin-bottom: 5px">
                                              <div  class="todo-task todo-task-complete todo-task-changing">
                                                <p>Название: ${el.properties.name}</p>
                                                  <div class="route-info">Адрес: ${el.properties.description}</div>
                                              </div>
                                            </div>
                                          `;
                                    console.log(htmlElement);
                                    console.log(htmlList);
                                    htmlList = htmlList + htmlElement;

                                });
                                console.log(newPoints);
                                dom.list.innerHTML = htmlList;
                                function init() {
                                    /**
                                     * Создание мультимаршрута.
                                     * @see https://api.yandex.ru/maps/doc/jsapi/2.1/ref/reference/multiRouter.MultiRoute.xml
                                     */





                                    var myPlacemark;
                                    console.log(points);
                                    var multiRoute = new ymaps.multiRouter.MultiRoute({
                                        referencePoints: points
                                    }, {
                                        // Тип промежуточных точек, которые могут быть добавлены при редактировании.
                                        editorMidPointsType: "via",
                                        // В режиме добавления новых путевых точек запрещаем ставить точки поверх объектов карты.
                                        editorDrawOver: false
                                    });

                                    var buttonEditor = new ymaps.control.Button({
                                        data: {content: "Режим редактирования"}
                                    });



                                    buttonEditor.events.add("select", function () {

                                        multiRoute.editor.start({
                                            addWayPoints: true,
                                            removeWayPoints: true
                                        });
                                    });

                                    buttonEditor.events.add("deselect", function () {
                                        // Выключение режима редактирования.
                                        multiRoute.editor.stop();
                                    });

                                    var buttonSave = new ymaps.control.Button({
                                        data: {content: "Сохранить"}
                                    });
                                    buttonSave.events.add("select", function () {
                                        newPoints = [];
                                        multiRoute.model.getAllPoints().map(point => {

                                            newPoints.push(point.getReferencePoint())
                                        });
                                        var jsonData = JSON.stringify(newPoints);
                                        var xhr2 = new XMLHttpRequest();
// Отправка данных на сервер с помощью AJAX-запроса
                                        let username = "admin";
                                        let password = "admin";
                                        let token = document.getElementById("csrf").value;
                                        let nameToken = document.getElementById("csrf_name").value;
                                        xhr2.open('POST', '/points/' + id, true);
                                        xhr2.setRequestHeader("Authorization", "Basic " + btoa(username + ":" + password));
                                        xhr2.setRequestHeader(nameToken, token);
                                        xhr2.setRequestHeader('Content-Type', 'application/json');
                                        xhr2.onreadystatechange = function () {
                                            if (xhr2.readyState === 4 && xhr2.status === 200) {
                                                // Запрос успешно обработан
                                                // var response = JSON.parse(xhr.responseText);
                                                // Действия с полученными данными
                                            }
                                        };
                                        console.log(newPoints);
                                        xhr2.send(jsonData);

                                    });
                                    // Создаем карту с добавленной на нее кнопкой.
                                    var myMap = new ymaps.Map('map', {
                                        center: [53.90356363622448,27.562299975309177],
                                        zoom: 12,
                                        controls: [buttonEditor , buttonSave]
                                    }, {
                                        buttonMaxWidth: 300,
                                        // restrictMapArea: [
                                        //     [51.68920899288854,27.773234360554287],
                                        //     [56.089621108656445,28.14779765636679]
                                        // ]
                                    });

                                    // Добавляем мультимаршрут на карту.
                                    myMap.geoObjects.add(multiRoute);

                                    // if(multiRoute.viapointremove)


                                    // Слушаем клик на карте.
                                    myMap.events.add('click', function (e) {
                                        var coords = e.get('coords');
                                        console.log(coords);
                                        // Если метка уже создана – просто передвигаем ее.
                                        // if (myPlacemark) {
                                        //     myPlacemark.geometry.setCoordinates(coords);
                                        // }
                                        // // Если нет – создаем.
                                        // else {
                                        //     myPlacemark = createPlacemark(coords);
                                        //     myMap.geoObjects.add(myPlacemark);
                                        //     // Слушаем событие окончания перетаскивания на метке.
                                        //     myPlacemark.events.add('dragend', function () {
                                        //         getAddress(myPlacemark.geometry.getCoordinates());
                                        //     });
                                        // }
                                        // getAddress(coords);
                                    });
                                    // console.log(multiRoute.getRoutes());
                                    // Создание метки.
                                    function createPlacemark(coords) {
                                        return new ymaps.Placemark(coords, {
                                            iconCaption: 'поиск...'
                                        }, {
                                            preset: 'islands#violetDotIconWithCaption',
                                            draggable: true
                                        });
                                    }


                                    // Определяем адрес по координатам (обратное геокодирование).
                                    function getAddress(coords) {
                                        myPlacemark.properties.set('iconCaption', 'поиск...');
                                        ymaps.geocode(coords).then(function (res) {
                                            var firstGeoObject = res.geoObjects.get(0);
                                            console.log(firstGeoObject)
                                            myPlacemark.properties
                                                .set({
                                                    // Формируем строку с данными об объекте.
                                                    iconCaption: [
                                                        // Название населенного пункта или вышестоящее административно-территориальное образование.
                                                        firstGeoObject.getLocalities().length ? firstGeoObject.getLocalities() : firstGeoObject.getAdministrativeAreas(),
                                                        // Получаем путь до топонима, если метод вернул null, запрашиваем наименование здания.
                                                        firstGeoObject.getThoroughfare() || firstGeoObject.getPremise()
                                                    ].filter(Boolean).join(', '),
                                                    // В качестве контента балуна задаем строку с адресом объекта.
                                                    balloonContent: firstGeoObject.getAddressLine()
                                                });
                                        });
                                    }
                                }


                                ymaps.ready(init);
                            }
                        }
                    }
                    xhr.open("GET", url, true);
                    xhr.send();





                }
                else {
                    function init() {
                        /**
                         * Создание мультимаршрута.
                         * @see https://api.yandex.ru/maps/doc/jsapi/2.1/ref/reference/multiRouter.MultiRoute.xml
                         */





                        var myPlacemark;
                        console.log(points);
                        var multiRoute = new ymaps.multiRouter.MultiRoute({
                            referencePoints: coordinates
                        }, {
                            // Тип промежуточных точек, которые могут быть добавлены при редактировании.
                            editorMidPointsType: "via",
                            // В режиме добавления новых путевых точек запрещаем ставить точки поверх объектов карты.
                            editorDrawOver: false
                        });

                        var buttonEditor = new ymaps.control.Button({
                            data: {content: "Режим редактирования"}
                        });



                        buttonEditor.events.add("select", function () {

                            multiRoute.editor.start({
                                addWayPoints: true,
                                removeWayPoints: true
                            });
                        });

                        buttonEditor.events.add("deselect", function () {
                            // Выключение режима редактирования.
                            multiRoute.editor.stop();
                        });

                        var buttonSave = new ymaps.control.Button({
                            data: {content: "Сохранить"}
                        });
                        buttonSave.events.add("select", function () {
                            newPoints = [];
                            multiRoute.model.getAllPoints().map(point => {

                                newPoints.push(point.getReferencePoint())
                            });
                            var jsonData = JSON.stringify(newPoints);
                            var xhr2 = new XMLHttpRequest();
// Отправка данных на сервер с помощью AJAX-запроса
                            let username = "admin";
                            let password = "admin";
                            let token = document.getElementById("csrf").value;
                            let nameToken = document.getElementById("csrf_name").value;
                            xhr2.open('POST', '/points/' + id, true);
                            xhr2.setRequestHeader("Authorization", "Basic " + btoa(username + ":" + password));
                            xhr2.setRequestHeader(nameToken, token);
                            xhr2.setRequestHeader('Content-Type', 'application/json');
                            if (newPoints.length === 2){
                                xhr2.onreadystatechange = function () {
                                    if (xhr2.readyState === 4 && xhr2.status === 200) {
                                        // Запрос успешно обработан
                                        // var response = JSON.parse(xhr.responseText);
                                        // Действия с полученными данными
                                    }
                                };
                                console.log(newPoints);
                                xhr2.send(jsonData);
                            } else {
                                xhr2.onreadystatechange = function () {
                                    if (xhr2.readyState === 4 && xhr2.status === 200) {
                                        // Запрос успешно обработан
                                        // var response = JSON.parse(xhr.responseText);
                                        // Действия с полученными данными
                                    }
                                };
                                console.log(newPoints);
                                xhr2.send(jsonData);
                            }


                        });
                        // Создаем карту с добавленной на нее кнопкой.
                        var myMap = new ymaps.Map('map', {
                            center: [53.90356363622448,27.562299975309177],
                            zoom: 12,
                            controls: [buttonEditor , buttonSave]
                        }, {
                            buttonMaxWidth: 300,
                            // restrictMapArea: [
                            //     [51.68920899288854,27.773234360554287],
                            //     [56.089621108656445,28.14779765636679]
                            // ]
                        });

                        // Добавляем мультимаршрут на карту.
                        myMap.geoObjects.add(multiRoute);

                        // if(multiRoute.viapointremove)


                        // Слушаем клик на карте.
                        myMap.events.add('click', function (e) {
                            var coords = e.get('coords');
                            console.log(coords);
                            // Если метка уже создана – просто передвигаем ее.
                            // if (myPlacemark) {
                            //     myPlacemark.geometry.setCoordinates(coords);
                            // }
                            // // Если нет – создаем.
                            // else {
                            //     myPlacemark = createPlacemark(coords);
                            //     myMap.geoObjects.add(myPlacemark);
                            //     // Слушаем событие окончания перетаскивания на метке.
                            //     myPlacemark.events.add('dragend', function () {
                            //         getAddress(myPlacemark.geometry.getCoordinates());
                            //     });
                            // }
                            // getAddress(coords);
                        });
                        // console.log(multiRoute.getRoutes());
                        // Создание метки.
                        function createPlacemark(coords) {
                            return new ymaps.Placemark(coords, {
                                iconCaption: 'поиск...'
                            }, {
                                preset: 'islands#violetDotIconWithCaption',
                                draggable: true
                            });
                        }


                        // Определяем адрес по координатам (обратное геокодирование).
                        function getAddress(coords) {
                            myPlacemark.properties.set('iconCaption', 'поиск...');
                            ymaps.geocode(coords).then(function (res) {
                                var firstGeoObject = res.geoObjects.get(0);
                                console.log(firstGeoObject)
                                myPlacemark.properties
                                    .set({
                                        // Формируем строку с данными об объекте.
                                        iconCaption: [
                                            // Название населенного пункта или вышестоящее административно-территориальное образование.
                                            firstGeoObject.getLocalities().length ? firstGeoObject.getLocalities() : firstGeoObject.getAdministrativeAreas(),
                                            // Получаем путь до топонима, если метод вернул null, запрашиваем наименование здания.
                                            firstGeoObject.getThoroughfare() || firstGeoObject.getPremise()
                                        ].filter(Boolean).join(', '),
                                        // В качестве контента балуна задаем строку с адресом объекта.
                                        balloonContent: firstGeoObject.getAddressLine()
                                    });
                            });
                        }
                    }


                    ymaps.ready(init);
                }
            }
        }
    }
    let username = "admin";
    let password = "admin";
    xhr.open("GET", "/points/" + id, true);
    xhr.setRequestHeader("Authorization", "Basic " + btoa(username + ":" + password));
    xhr.send();
}



getPoints(id);



