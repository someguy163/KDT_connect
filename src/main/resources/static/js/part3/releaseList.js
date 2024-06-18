// 출고 버튼 클릭 시
function releaseProduct(productId) {
    // 선택된 품목의 정보 가져오기 (productName과 itemCode만 가져오는 것으로 가정)
    var productName = $('[data-product="' + productId + '"]').find('.productName').text();
    var itemCode = $('[data-product="' + productId + '"]').find('.itemCode').text();

    // 아래 테이블에 추가
    var newRow = '<tr>' +
        '<td></td>' + // 라디오 버튼 칸
        '<td>' + productName + '</td>' +
        '<td>' + itemCode + '</td>' +
        '<td></td>' + // 품목명, 조달납기일, 생산시작일, 생산종료일, 입고수량은 데이터베이스에서 가져와야 함
        '<td></td>' +
        '<td></td>' +
        '<td></td>' +
        '<td></td>' +
        '<td></td>' +
        '<td><input type="text" style="width: 80px;"></td>' +
        '</tr>';
    $('#releaseTable tbody').append(newRow);
}