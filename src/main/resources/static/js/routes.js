var xhr = new XMLHttpRequest();

const dom= {
    list: document.getElementById("routes"),
};

let routes = [];
let copyRouteList= [];
xhr.onreadystatechange = function() {
    if (xhr.readyState === 4) {
        if (xhr.status === 200) {
            var json = JSON.parse(xhr.responseText);

            // Обработка полученного списка точек
            // console.log(json);
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
                // console.log(routes);
            }
        }
    }
}
let username = "admin";
let password = "admin";
xhr.open("GET", "/routes", true);
xhr.setRequestHeader("Authorization", "Basic " + btoa(username + ":" + password));
xhr.send();

function routeRender(list) {
    let htmlList = "";
    for(var i = 0;i < list.length; i ++) {
        let routeHtml = `
        <div id="${list[i].id}" class="todo-task-container">
        <div  class="route todo-task-complete todo-task-changing">
          <p >${list[i].name}</p>
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
    // console.log(copyRouteList);
    let inputValue = document.getElementById("search-input").value;
    copyRouteList.forEach(route => {
        if(route.name.toLowerCase().includes(inputValue.toLowerCase())) {
            routes.push(route);
            console.log(route);
        }
    });
    ;
    routeRender(routes);
}
//


