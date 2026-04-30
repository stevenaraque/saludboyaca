package co.sena.cimm.adso.saludboyaca.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "CaptchaGenerator", urlPatterns = {"/captcha"})
public class CaptchaGenerator extends HttpServlet {
    
    private static final int WIDTH = 160;
    private static final int HEIGHT = 50;
    private static final int CODE_LENGTH = 6;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Generar código aleatorio
        String captchaCode = generateRandomCode();
        
        // Guardar en sesión
        HttpSession session = request.getSession();
        session.setAttribute("captchaCode", captchaCode);
        
        // Crear imagen
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        
        // Fondo
        g2d.setColor(new Color(234, 240, 247));
        g2d.fillRect(0, 0, WIDTH, HEIGHT);
        
        // Líneas de ruido
        Random random = new Random();
        g2d.setColor(new Color(26, 82, 118));
        for (int i = 0; i < 15; i++) {
            int x1 = random.nextInt(WIDTH);
            int y1 = random.nextInt(HEIGHT);
            int x2 = random.nextInt(WIDTH);
            int y2 = random.nextInt(HEIGHT);
            g2d.drawLine(x1, y1, x2, y2);
        }
        
        // Dibujar código
        g2d.setFont(new Font("Arial", Font.BOLD, 28));
        g2d.setColor(new Color(108, 52, 131));
        
        int x = 20;
        for (int i = 0; i < captchaCode.length(); i++) {
            // Rotación aleatoria por carácter
            int rotation = random.nextInt(20) - 10;
            g2d.rotate(Math.toRadians(rotation), x, 30);
            g2d.drawString(String.valueOf(captchaCode.charAt(i)), x, 35);
            g2d.rotate(Math.toRadians(-rotation), x, 30);
            x += 22;
        }
        
        // Puntos de ruido
        g2d.setColor(Color.GRAY);
        for (int i = 0; i < 50; i++) {
            int x1 = random.nextInt(WIDTH);
            int y1 = random.nextInt(HEIGHT);
            g2d.fillRect(x1, y1, 2, 2);
        }
        
        g2d.dispose();
        
        // Enviar imagen
        response.setContentType("image/png");
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        response.getOutputStream().write(baos.toByteArray());
    }
    
    private String generateRandomCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        
        for (int i = 0; i < CODE_LENGTH; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}