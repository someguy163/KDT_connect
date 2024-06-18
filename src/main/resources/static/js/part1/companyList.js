//js를 통해 리스트와 아작스를 연결
$(document).ready(function () { // 페이지가 로딩되는 순간 바로 실행
    console.log("ready!");
    companyListAjax(1); // 들어가서 바로 1페이지가 보임, 아래 펑션의 이름
});

function companyListAjax(page) { // 위에서 보낸 매개변수 1을 받아 준다!
    const innerHtml = $("#companyList"); // innerHtml의 위치를 선정해줌 (html의 아이디값과 일치 시킴!)
    const f = document.getElementById("form1"); //part1_insert_company.html의 form1과연결
    f.page.value = page;

    $.ajax({
        url: "/part1/companyListAjax", //백엔드 경로
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


// 업체 중복 체크하는 function

let isCheck = 0; //제품 코드 체크 여부 확인 (사업자번호 중복일 경우 = 0 , 중복이 아닐 경우 = 1)

function businessIdCheckAjax() {
    const businessId = $("#businessId").val();  // 내가 회원가입한 아이디 alsrl가 username으로 들어감
    console.log("businessId : ", businessId);
    if(businessId !== ''){ // username이 빈값이 아닐때 아작스 호출하라는 의미
        $.ajax({
            url: "/part1/businessIdCheck",  //우리가 만든 백엔드 url
            type: 'GET',//get매핑이니까 get 타입
            cache: false,
            data: {businessId : businessId}, //프론트엔드에서 백엔드로 보내주는 값(signup.html >> UserController)
            // 이제 username 에 alsrl가 들어가서 백엔드에서 @RequestParam String username 요렇게 매개변수로 받을수잇져!
            dataType:'json',   //return 타입을 의미한다!
            // 여기까지 api 쏠 준비 완료
            async: true,
            success: function (cnt) {
                if (cnt > 0) { // 아이디 중복
                    alert("중복되는 사업자 번호입니다. 다른 사업자 번호를 입력하세요");
                    $("#businessId").attr("readonly",false)
                    $("#businessIdCheck").attr("disabled",false)
                    $("#businessId").focus();
                    isCheck = 0;
                } else { // 아이디 사용 가능
                    alert("등록 가능한 업체입니다.");
                    $("#businessId").attr("readonly",true)
                    $("#businessIdCheck").attr("disabled",true)
                    $("#comAdd").focus();
                    isCheck = 1;
                }
                setTimeout(function () {
                }, 1000)
            },
            error: function (e) {
                console.log("error : "+e)
            }
        })
    }else {
        alert("사업자 번호를 입력하세요.");
        $("#businessId").focus();
    }

}