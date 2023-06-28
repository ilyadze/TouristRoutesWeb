var xhr = new XMLHttpRequest();
const urlParams = new URLSearchParams(window.location.search);
const id = urlParams.get('id');
console.log(id);

const dom= {
    list: document.getElementById("route-list"),
};

let copyRouteList= [];
function getRoutes() {
    let routes = [];

    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                var json = JSON.parse(xhr.responseText);
                // Обработка полученного списка точек
                for (var i = 0; i < json.length; i++) {
                    const route = {
                        id: json[i].id,
                        category: json[i].category,
                        name:  json[i].name,
                        description: json[i].description,
                        distance: json[i].distance,
                    };
                    routes.push(route);
                    copyRouteList.push(route);
                }
            }
        }
    }
    let username = "admin";
    let password = "admin";
    xhr.open("GET", "/routes", true);
    xhr.setRequestHeader("Authorization", "Basic " + btoa(username + ":" + password));
    xhr.send();
    return routes;
}

let routes = getRoutes();

function routeRender(list) {
    let htmlList = "";
    for(var i = 0;i < list.length; i ++) {
        let routeHtml = `
        <div id="${list[i].id}" class="todo-task-container">
          <div class="todo-task todo-task-complete todo-task-changing">
            <p>${list[i].name}</p>
            <div class="route-info"><a href="/routesPage/${list[i].id}">...</a></div>
          </div>
        </div>`;
        htmlList = htmlList + routeHtml;
    }
    dom.list.innerHTML = htmlList;
}

function findRoute() {
    let length = routes.length;
    for (let i = 0;i < length; i++) {
        routes.pop();
    }
    let inputValue = document.getElementById("search-input").value;
    copyRouteList.forEach(route => {
        if(route.name.toLowerCase().includes(inputValue.toLowerCase())) {
            routes.push(route);
        }
    });

    routeRender(routes);
}
//

var coordinates = [];
function getPoints(id) {
//https://search-maps.yandex.ru/v1/?text=Рай,Россия&type=geo&lang=ru_RU&apikey=<API-ключ>
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                var json = JSON.parse(xhr.responseText);
                coordinates = json.map(point => [point.latitude, point.longitude]);
                // console.log('1---------');
                // console.log(coordinates);
                // ymaps.ready(init);
                function init() {
                    // Объявляем набор опорных точек и массив индексов транзитных точек.



                    var referencePoints = coordinates,
                        viaIndexes = [2];
                    console.log('ref---------');
                    console.log(referencePoints);

                    var multiRoute = new ymaps.multiRouter.MultiRoute({
                        referencePoints: referencePoints,
                        params: {viaIndexes: viaIndexes}
                    }, {
                        // Внешний вид путевых точек.
                        wayPointStartIconColor: "#333",
                        wayPointStartIconFillColor: "#B3B3B3",
                        // Задаем собственную картинку для последней путевой точки.
                        // wayPointFinishIconLayout: "default#image",
                        // wayPointFinishIconImageHref: "default#image",
                        wayPointFinishIconImageSize: [30, 30],
                        wayPointFinishIconImageOffset: [-15, -15],
                        // Позволяет скрыть иконки путевых точек маршрута.
                        // wayPointVisible:false,

                        // Внешний вид транзитных точек.
                        viaPointIconRadius: 7,
                        viaPointIconFillColor: "#000088",
                        viaPointActiveIconFillColor: "#E63E92",
                        // Транзитные точки можно перетаскивать, при этом
                        // маршрут будет перестраиваться.
                        viaPointDraggable: true,
                        // Позволяет скрыть иконки транзитных точек маршрута.
                        // viaPointVisible:false,

                        // Внешний вид точечных маркеров под путевыми точками.
                        pinIconFillColor: "#000088",
                        pinActiveIconFillColor: "#B3B3B3",
                        // Позволяет скрыть точечные маркеры путевых точек.
                        // pinVisible:false,

                        // Внешний вид линии маршрута.
                        routeStrokeWidth: 2,
                        routeStrokeColor: "#000088",
                        routeActiveStrokeWidth: 6,
                        routeActiveStrokeColor: "#E63E92",

                        // Внешний вид линии пешеходного маршрута.
                        routeActivePedestrianSegmentStrokeStyle: "solid",
                        routeActivePedestrianSegmentStrokeColor: "#00CDCD",

                        // Автоматически устанавливать границы карты так, чтобы маршрут был виден целиком.
                        boundsAutoApply: true
                    });


                    // Создаем карту с добавленной на нее кнопкой.
                    var myMap = new ymaps.Map('map', {
                        center: [53.90356363622448,27.562299975309177],
                        zoom: 12,
                        // controls: [removePointsButton, routingModeButton]
                    }, {
                        buttonMaxWidth: 300,
                        // restrictMapArea: [
                        //     [51.68920899288854,27.773234360554287],
                        //     [56.089621108656445,28.14779765636679]
                        // ]
                    });

                    // Добавляем мультимаршрут на карту.
                    myMap.geoObjects.add(multiRoute);
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
    // console.log('2---------');
    // console.log(coordinates);
    // console.timeStamp(70);
    // return coordinates;
}
// getPoints();

dom.list.onclick = (event) => {
    const target = event.target;
    // console.log('target------------------');
    // console.log(typeof target);
    let id = target.id;
    console.log('id------------------');
    console.log(target.id)
    console.log(target.classList);
    const key = 'id';
    const value = id;
    if (target.classList.contains("todo-task")) {
        // if(target.id)
        const url = new URL(window.location.href);

        url.searchParams.set(key, value);

        const newUrl = url.href;

        window.location.href = newUrl;
    }

}


if( id!=null) {
    getPoints(id);
}
else {
    function init() {
        // Объявляем набор опорных точек и массив индексов транзитных точек.

        // Создаем карту с добавленной на нее кнопкой.
        var myMap = new ymaps.Map('map', {
            center: [53.90356363622448,27.562299975309177],
            zoom: 12,
            // controls: ['smallMapDefaultSet']
        }, {
            buttonMaxWidth: 300,
            // restrictMapArea: [
            //     [51.68920899288854,27.773234360554287],
            //     [56.089621108656445,28.14779765636679]
            // ]
        });

    }
    ymaps.ready(init);
}

//