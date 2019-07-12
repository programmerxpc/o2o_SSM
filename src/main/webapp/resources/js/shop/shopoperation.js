/**
 *
 */
$(function () {
    var shopId = getQueryString('shopId');
    var isEdit = shopId ? true : false;
    var initUrl = '/o2o/shopadmin/getshopinitinfo';
    var registerShopUrl = '/o2o/shopadmin/registershop';
    var shopInfoUrl = '/o2o/shopadmin/getshopbyid?shopId=' + shopId;
    var editShopUrl = '/o2o/shopadmin/modifyshop';
    if (!isEdit) {
        getShopInitInfo();
    }else {
        getShopInfo(shopId);
    }

    //根据shopId获取店铺信息
    function getShopInfo(shopId) {
        $.getJSON(shopInfoUrl,function (data) {
            if (data.success) {
                var shop = data.shop;
                $('#shop-name').val(shop.shopName);
                $('#shop-addr').val(shop.shopAddr);
                $('#shop-phone').val(shop.phone);
                $('#shop-desc').val(shop.shopDesc);
                var shopCategoryHtml = '<option data-id="' + shop.shopCategory.shopCategoryId + '" selected>' + shop.shopCategory.shopCategoryName + '</option>';
                var tempAreaHtml = '';
                data.areaList.map(function (item, index) {
                    tempAreaHtml += '<option data-id="' + item.areaId + '">' + item.areaName + '</option>';
                });
                $('#shop-catagory').html(shopCategoryHtml);
                $('#shop-catagory').attr('disabled','disabled');//不能选择
                $('#area').html(tempAreaHtml);
                $("#area option[data-id='"+shop.area.areaId+"']").attr("selected","selected");
            }
        });
    }

    //获取店铺基本信息
    function getShopInitInfo() {
        $.getJSON(initUrl,function (data) {
            if (data.success){
                var tempHtml = '';
                var tempAreaHtml = '';
                data.shopCategoryList.map(function (item, index) {
                    tempHtml += '<option data-id="' + item.shopCategoryId + '">' + item.shopCategoryName + '</option>';
                });
                data.areaList.map(function (item, index) {
                    tempAreaHtml += '<option data-id="' + item.areaId + '">' + item.areaName + '</option>';
                });
                $('#shop-catagory').html(tempHtml);
                $('#area').html(tempAreaHtml);
            }
        });
    }

    //点击提交按钮，注册店铺
    $('#submit').click(function () {
        var shop ={};
        if (isEdit) {//如果是编辑操作
            shop.shopId = shopId;
        }
        shop.shopName = $('#shop-name').val();
        shop.shopAddr = $('#shop-addr').val();
        shop.phone = $('#shop-phone').val();
        shop.shopDesc = $('#shop-desc').val();
        shop.shopCategory = {
            shopCategoryId : $('#shop-catagory').find('option').not(function () {
                return !this.selected;
            }).data('id')
        };
        shop.area = {
            areaId : $('#area').find('option').not(function () {
                return !this.selected;
            }).data('id')
        };
        var shopImg = $('#shop-img')[0].files[0];
        var formData = new FormData();
        formData.append('shopImg',shopImg);
        formData.append('shopStr',JSON.stringify(shop));
        var verifyCodeActual = $('#j_kaptcha').val();
        if (!verifyCodeActual) {
            $.toast('请输入验证码!');
            return;
        }
        formData.append('verifyCodeActual',verifyCodeActual);

        $.ajax({
            url: (isEdit?editShopUrl:registerShopUrl),
            type: 'POST',
            data: formData,
            contentType : false,
            processData : false,
            cache: false,
            success: function (data) {
                if (data.success) {
                    $.toast('提交成功!');
                }else {
                    $.toast('提交失败!' + data.errMsg);
                }
                $('#kaptcha_img').click();//提交之后更换验证码
            }
        });
    });
});