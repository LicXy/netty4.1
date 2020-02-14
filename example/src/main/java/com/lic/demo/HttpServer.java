package com.lic.demo;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
public class HttpServer {
    public void openServer(int port){
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.channel(NioServerSocketChannel.class);  //接收客户端请求Channel
        EventLoopGroup boss = new NioEventLoopGroup(1);  //boss线程组
        EventLoopGroup work = new NioEventLoopGroup(2);  //work线程组
        bootstrap.group(boss,work);
        bootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                ch.pipeline().addLast("http-decoder",new HttpRequestDecoder()); //request解码
                //ch.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65536)); //将请求头与请求体聚合(FullHttpRequest)
                ch.pipeline().addLast("http-encode",new HttpResponseEncoder());  //resposen编码
                ch.pipeline().addLast("http-service",new HttpServerHandler());  //业务逻辑处理
            }
        });

        try {
            ChannelFuture cf = bootstrap.bind(port).sync();
            System.out.println("服务已启动, 端口为:"+port);
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            //出现异常,释放线程池资源
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }
    }

    public class HttpServerHandler extends SimpleChannelInboundHandler{
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
            //写入请求头
            response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/html;charset=UTF-8");
            response.content().writeBytes("Hello Netty".getBytes());
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        }
    }

    public static void main(String[] args) {
        new HttpServer().openServer(8080);
    }
}
