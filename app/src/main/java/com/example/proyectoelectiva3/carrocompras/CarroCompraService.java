package com.example.proyectoelectiva3.carrocompras;

import com.example.proyectoelectiva3.admin.AdminUtils;
import com.example.proyectoelectiva3.admin.articulosEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CarroCompraService {

    private CarroComprasRepository repoDB;


    public String modificar_CarroCompras()
    {
        return "";
    }

    public boolean existeProductoEnCarro(CarroComprasModel carro, String newItemId)
    {
        boolean flag = false;

        for (ItemCarroCompraModel i: carro.getItems()) {
            if (i.getIdProducto().equals(newItemId))
            {
                flag = true;
                break;
            }
        }
        return flag;
    }

    public void insertar_CarroCompras(final String idCliente, final String idProducto, final IStringResultProcess callBackResultProcess)
    {

        repoDB = new CarroComprasRepository();

        repoDB.getCarroComprasByCliente(idCliente, new IGetSingleObject<CarroComprasModel>() {
            @Override
            public void onCallBackSuccess(final CarroComprasModel carro) {

                if (carro != null) //Se va agregar un nuevo producto
                {
                    if (!existeProductoEnCarro(carro,idProducto)) //No poder ingresar el mismo producto en el carro...
                    {
                        repoDB.getDetalleProducto(idProducto, new IGetSingleObject<articulosEntity>() {
                            @Override
                            public void onCallBackSuccess(articulosEntity prod)
                            {
                                if (prod != null)
                                {
                                    ItemCarroCompraModel item = new ItemCarroCompraModel();
                                    item.setIdItem(UUID.randomUUID().toString());
                                    item.setIdProducto(idProducto);

                                    item.setCantidad(1);
                                    item.setNombreImagen(prod.getImageName());
                                    item.setNombreProducto(prod.getNombre());
                                    item.setIdCarroCompras(carro.getIdCart());

                                    if (prod.getPrecio() != null && !prod.getPrecio().isEmpty())
                                        item.setPrecio( Double.parseDouble(prod.getPrecio()));
                                    else
                                        item.setPrecio(0D);

                                    if (prod.getDescuento() != null && !prod.getDescuento().isEmpty())
                                        item.setDescuento( Double.parseDouble(prod.getDescuento())  * item.getPrecio() ) ;
                                    else
                                        item.setDescuento(0D);

                                    if (prod.getRebaja() != null && !prod.getRebaja().isEmpty())
                                        item.setRebaja( Double.parseDouble(prod.getRebaja()));
                                    else
                                        item.setRebaja(0D);

                                    item.setSubTotal(item.getPrecio() * item.getCantidad());

                                    if (item.getPrecio() > 0)
                                        item.setTotal(item.getSubTotal() - (item.getDescuento() * item.getCantidad() ) - (item.getRebaja() * item.getCantidad()));
                                    else
                                        item.setTotal(0D);

                                    carro.getItems().add(item);

                                    //Totales del carrito de compras
                                    carro.setImpuestos(0D);
                                    Double totalDescuentos = 0D, totalSubTotal = 0D, totalRebajas = 0D;
                                    for (ItemCarroCompraModel i: carro.getItems()) {
                                        totalDescuentos += i.getDescuento();
                                        totalSubTotal += i.getSubTotal();
                                        totalRebajas += i.getRebaja();
                                    }
                                    carro.setDescuentos(totalDescuentos);
                                    carro.setSubTotal(totalSubTotal);
                                    carro.setRebajas(totalRebajas);
                                    carro.setTotalDescuentos(totalDescuentos + totalRebajas);
                                    carro.setTotal(totalSubTotal-totalDescuentos-totalRebajas);

                                    //Guardar los datos
                                    //callBackResultProcess.onCallBackSuccess(repoDB.modificarCarroCompras(carro));
                                    repoDB.modificarCarroCompras(carro, new IStringResultProcess() {
                                        @Override
                                        public void onCallBackSuccess(String result) {
                                            callBackResultProcess.onCallBackSuccess(result);
                                        }

                                        @Override
                                        public void onCallBackFail(String msjError) {
                                            callBackResultProcess.onCallBackFail(msjError);
                                        }
                                    });

                                }
                                else
                                {
                                    callBackResultProcess.onCallBackSuccess("ERR");
                                }

                            }

                            @Override
                            public void onCallBackFail(String msjError) {
                                callBackResultProcess.onCallBackFail(msjError);
                            }

                        });


                    }
                    else //NO crear un item duplicado
                    {
                        callBackResultProcess.onCallBackSuccess("DUP");
                    }
                }
                else //Se va crear un nuevo carro, con el primer producto
                {

                    repoDB.getDetalleProducto(idProducto, new IGetSingleObject<articulosEntity>() {
                        @Override
                        public void onCallBackSuccess(articulosEntity prod) {

                            CarroComprasModel car = new CarroComprasModel();
                            List<ItemCarroCompraModel> listItems = new ArrayList<>();
                            ItemCarroCompraModel item = new ItemCarroCompraModel();

                            car.setIdCart(UUID.randomUUID().toString());
                            car.setFechaCreacion(AdminUtils.getNowDateToString());
                            car.setIdCliente(idCliente);

                            if (prod != null)
                            {
                                item.setIdItem(UUID.randomUUID().toString());
                                item.setIdProducto(idProducto);
                                item.setCantidad(1);
                                item.setNombreImagen(prod.getImageName());
                                item.setNombreProducto(prod.getNombre());
                                item.setIdCarroCompras(car.getIdCart());

                                if (prod.getPrecio() != null && !prod.getPrecio().isEmpty())
                                    item.setPrecio( Double.parseDouble(prod.getPrecio()));
                                else
                                    item.setPrecio(0D);

                                if (prod.getDescuento() != null && !prod.getDescuento().isEmpty())
                                    item.setDescuento( Double.parseDouble(prod.getDescuento()) * item.getPrecio());
                                else
                                    item.setDescuento(0D);


                                if (prod.getRebaja() != null && !prod.getRebaja().isEmpty())
                                    item.setRebaja( Double.parseDouble(prod.getRebaja()));
                                else
                                    item.setRebaja(0D);

                                item.setSubTotal(item.getPrecio() * item.getCantidad());
                                if (item.getPrecio() > 0)
                                    item.setTotal(item.getPrecio() - item.getDescuento() - item.getRebaja());
                                else
                                    item.setTotal(0D);

                                listItems.add(item);

                                car.setItems(listItems);

                                //Totales del carrito de compras
                                car.setImpuestos(0D);
                                car.setDescuentos(item.getDescuento());
                                car.setSubTotal(item.getSubTotal());
                                car.setRebajas(item.getRebaja());
                                car.setTotalDescuentos(item.getRebaja() + item.getDescuento());
                                car.setTotal(item.getTotal());

                                //Guardar los datos
                                //callBackResultProcess.onCallBackSuccess(repoDB.crearCarroCompras(car));
                                repoDB.crearCarroCompras(car, new IStringResultProcess() {
                                    @Override
                                    public void onCallBackSuccess(String result) {
                                        callBackResultProcess.onCallBackSuccess(result);
                                    }

                                    @Override
                                    public void onCallBackFail(String msjError) {
                                        callBackResultProcess.onCallBackFail(msjError);
                                    }
                                });
                            }
                            else
                            {
                                callBackResultProcess.onCallBackSuccess("ERR");
                            }

                        }

                        @Override
                        public void onCallBackFail(String msjError) {
                            callBackResultProcess.onCallBackFail(msjError);
                        }
                    });


                }
            }

            @Override
            public void onCallBackFail(String msjError) {
                callBackResultProcess.onCallBackFail(msjError);
            }

        });

    }

}
