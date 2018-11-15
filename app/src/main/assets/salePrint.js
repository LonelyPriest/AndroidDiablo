function loadPrintServer() {
    if (needCLodop()) {
           var server = PrintJs.getPrintHttpServer();
           PrintJs.log("print server:" + server);
           loadCLodop(server);
       }
}

function print() {
   var pageWidth = parseFloat(PrintJs.getPaperWidth());
   PrintJs.log("pageWidth:" + pageWidth.toString());

   var pageHeight = 14;
   var strBodyStyle="<style>"
        + ".table-response {min-height: .01%; overflow-x:auto;}"
        + "table {border-spacing:0; border-collapse:collapse; width:100%}"
        + "td,th {padding:0; border:1 solid #000000; text-align:center;}"
        + ".table-bordered {border:1 solid #000000;}"
        + "</style>";

   PrintJs.log("start print");
   
   var LODOP = getLodop();
   PrintJs.log(LODOP.CVERSION);

   if (LODOP.CVERSION) {
       LODOP.PRINT_INIT("task_print_sale_new");
       if (pageHeight === 0)
           LODOP.SET_PRINT_PAGESIZE(3, pageWidth * 100, pageHeight * 100, "");
       else
           LODOP.SET_PRINT_PAGESIZE(0, pageWidth * 100, pageHeight * 100, "");

       LODOP.SET_PRINT_MODE("PROGRAM_CONTENT_BYVAR", true);

        // PrintJs.log(document.getElementById("saleNew").innerHTML);
       LODOP.ADD_PRINT_HTM(
                       "3%", "3%",  "98%", "BottomMargin:0mm",
                       strBodyStyle + "<body>" + document.getElementById("saleNew").innerHTML + "</body>");

       LODOP.On_Return = function(taskId, value) {

       }

       LODOP.PREVIEW();
   }
}

var salePrintApp = angular.module('salePrintApp', []);

salePrintApp.controller('salePrintCtrl', function($scope) {
    // $scope.shop = "BuXianhui";
    loadPrintServer();

    var to_decimal = function(dight){
       var d = Math.round(dight * Math.pow(10,2)) / Math.pow(10,2);
       return d;
    };

    var d = angular.fromJson(PrintJs.getSaleCalc());
    var ss = angular.fromJson(PrintJs.getSaleStock());

    $scope.sale = {};
    $scope.sale.rsn = PrintJs.getSaleRSN();
    $scope.sale.shop = PrintJs.getSaleShop();
    $scope.sale.shopAddress = PrintJs.getShopAddress();
    $scope.sale.retailer = PrintJs.getSaleRetailer();
    $scope.sale.employee = PrintJs.getEmployee();

    $scope.sale.comment = d.comment;
    $scope.sale.datetime = d.datetime;
    $scope.sale.hasPay = d.hasPay;
    $scope.sale.cash = d.cash;
    $scope.sale.card = d.card;
    $scope.sale.wire = d.wire;
    $scope.sale.balance = d.balance;
    $scope.sale.accBalance = d.accBalance;
    // $scope.sale.total = d.total;
    // $scope.sale.calcBalance = d.shouldPay;

    $scope.sale.sellTotal = PrintJs.getSellTotal();
    $scope.sale.rejectTotal = PrintJs.getRejectTotal();


    $scope.sale.total = 0;
    $scope.sale.calcBalance = 0;

    $scope.stocks = [];
    angular.forEach(ss, function(s) {
        $scope.sale.total += s.saleTotal;
        var calc = to_decimal(s.finalPrice * s.discount * s.saleTotal / 100);
        $scope.sale.calcBalance += calc;

        $scope.stocks.push({
            orderId:     s.orderId,
            styleNumber: s.styleNumber,
            brand:       s.brand,
            total:       s.saleTotal,
            fprice:      s.finalPrice,
            fdiscount:   s.discount,
            calc:        calc,
            comment:     s.comment
        });
    });

    PrintJs.log(angular.toJson($scope.stocks));
    $scope.sale.calcBalance = Math.round($scope.sale.calcBalance);
    $scope.sale.curBalance = $scope.sale.calcBalance - $scope.sale.hasPay;
    
    PrintJs.log(angular.toJson($scope.sale));

    $scope.comments = [];
    var cc = angular.fromJson(PrintJs.getComments());
    var order = 0;
    angular.forEach(cc, function(c){
        order++;
        $scope.comments.push({
            order: order.toString(),
            comment: c
        });
    })

    var phones = angular.fromJson(PrintJs.getPhones());
    $scope.phones1 = phones.slice(0, 2);
    $scope.phones2 = phones.slice(2);

    $scope.bankCards = angular.fromJson(PrintJs.getBankCards());

});