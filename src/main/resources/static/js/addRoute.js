var xhr = new XMLHttpRequest();

newPoints = [];
var apiKey = '4f6d9c50-e543-4666-8734-47f93bb2268d';
function init() {

    var multiRoute = new ymaps.multiRouter.MultiRoute({
        referencePoints: []
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
        multiRoute.model.getAllPoints().map(point => {

            newPoints.push(point.getReferencePoint())
        });

        if (newPoints.length === 2) {
            const [x1, y1] = newPoints[0];
            const [x2, y2] = newPoints[1];
            console.log(newPoints);

            console.log('x1:', x1);
            console.log('y1:', y1);
            console.log('x2:', x2);
            console.log('y2:', y2);

            xhr.onreadystatechange = function() {
                if (xhr.readyState === 4) {
                    if (xhr.status === 200) {
                        var json = JSON.parse(xhr.responseText);

                        console.log(json);
                    }
                }
            }
            let coordenate1= (x1+x2)/2;
            let coordenate2=(y1+y2)/2;
            console.log(coordenate2.toFixed(5) + "----" + coordenate1.toFixed(5));
            let r1 = Math.abs(x1 -x2);
            let r2 = Math.abs(y1 -y2);
            let r = (r1 + r2)*2;
            console.log(r1 + "-----" + r2);

            // let url = "https://search-maps.yandex.ru/v1/?text=school&ll=" + coordenate1 + "," + coordenate2 +"&spn="+ r + "," + r +"&type=biz&lang=ru_RU&apikey=" + apiKey;
            let url = "https://search-maps.yandex.ru/v1/?text=%attractions&bbox=" + y2.toFixed(5)  + "," + x2.toFixed(5) + "~" + y1.toFixed(5)  + "," + x1.toFixed(5) + "&type=biz&lang=ru_RU&apikey=" + apiKey;
            // let url = "https://search-maps.yandex.ru/v1/?text=%школа&bbox=27.31051,53.82742~27.96969,54.10532&type=geo&lang=ru_RU&apikey=4f6d9c50-e543-4666-8734-47f93bb2268d";
            console.log(url);
            xhr.open("GET", url, true);
            xhr.send();
        }

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
        console.log(newPoints);
    });






    // Создаем карту с добавленной на нее кнопкой.
    var myMap = new ymaps.Map('map', {
        center: [53.90356363622448,27.562299975309177],
        zoom: 12,
        controls: [buttonEditor, buttonSave]
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


// var apiKey = 'e0bc5d1d-94da-42f9-a4e0-cd7d994894d1&lang=ru_RU';
// var startPoint = '55.753215,37.622504'; // Начальные координаты (широта и долгота)
// var endPoint = '55.751903,37.618447'; // Конечные координаты (широта и долгота)
// // "https://api-maps.yandex.ru/2.1/?apikey=e0bc5d1d-94da-42f9-a4e0-cd7d994894d1&lang=ru_RU"
// var requestUrl = 'https://api-maps.yandex.ru/2.1/?apikey=' + apiKey +
//     '&routeType=auto';



// var apiKey = '4f6d9c50-e543-4666-8734-47f93bb2268d';
//
// xhr.onreadystatechange = function() {
//     if (xhr.readyState === 4) {
//         if (xhr.status === 200) {
//             var json = JSON.parse(xhr.responseText);
//             console.log(json);
//         }
//     }
// }
// xhr.open("GET", "https://search-maps.yandex.ru/v1/?text=музеи%Минск&ll=40.17248,60.594641&spn=3.552069,2.400552&type=geo&lang=ru_RU&apikey=" + apiKey, true);
// xhr.send();


//https://search-maps.yandex.ru/v1/?text=деревня Толстик&type=geo&lang=ru_RU&apikey=<e0bc5d1d-94da-42f9-a4e0-cd7d994894d1>
//https://search-maps.yandex.ru/v1/?text=Москва, ул. Крылатские холмы&lang=ru_RU&apikey=<4f6d9c50-e543-4666-8734-47f93bb2268d>&callback=my_callback
