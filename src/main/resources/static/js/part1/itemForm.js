//js를 통해 리스트와 아작스를 연결
$(document).ready(function () { // 페이지가 로딩되는 순간 바로 실행
    console.log("ready!");
    itemListAjax(1); // 들어가서 바로 1페이지가 보임, 아래 펑션의 이름
});

function itemListAjax(page) { // 위에서 보낸 매개변수 1을 받아 준다!
    const innerHtml = $("#itemList"); // innerHtml의 위치를 선정해줌 (html의 아이디값과 일치 시킴!)
    const f = document.getElementById("form1"); //part1_insert_company.html의 form1과연결
    f.page.value = page;

    $.ajax({
        url: "/part1/itemListAjax", //백엔드 경로
        type: 'GET',
        cache: false,
        data: $('#form1').serialize(),
        dataType: "html",
        async: false,
        //성공 시에 part1_insert_company.html의 form태그 위치에 백엔드 경로(part1_insert_company_ajax)에 연결 된 리턴 값인 프론트 cartListAjax.html을 넣어서 보여줌
        success: function (data) {
            $(innerHtml).html(data)

            setTimeout(function () {
            }, 1000)
        },
        error: function (e) {
            $(innerHtml).html("")
        }
    })
}
function updateUnitSelectedOption() {
    var selectedValue = document.getElementById('unitSelect').value;
    document.querySelectorAll('#unitSelect option').forEach(function(option) {
        if (option.value === selectedValue) {
            option.setAttribute('selected', 'selected');
        } else {
            option.removeAttribute('selected');
        }
    });
}

function updateAssySelectedOption() {
    var selectedValue = document.getElementById('assySelect').value;
    document.querySelectorAll('#assySelect option').forEach(function(option) {
        if (option.value === selectedValue) {
            option.setAttribute('selected', 'selected');
        } else {
            option.removeAttribute('selected');
        }
    });
}

function updatePartSelectedOption() {
    var selectedValue = document.getElementById('partSelect').value;
    document.querySelectorAll('#partSelect option').forEach(function(option) {
        if (option.value === selectedValue) {
            option.setAttribute('selected', 'selected');
        } else {
            option.removeAttribute('selected');
        }
    });
}