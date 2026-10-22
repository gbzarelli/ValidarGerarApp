from pathlib import Path

from PIL import Image, ImageDraw, ImageFont


ROOT = Path(__file__).resolve().parent
OUT = ROOT.parent.parent / "app" / "src" / "main" / "res"


def gradient(size, top, bottom):
    width, height = size
    image = Image.new("RGB", size)
    pixels = image.load()
    for y in range(height):
        ratio = y / max(1, height - 1)
        color = tuple(round(top[i] * (1 - ratio) + bottom[i] * ratio) for i in range(3))
        for x in range(width):
            pixels[x, y] = color
    return image


def draw_document(draw, box, scale=1.0, shadow=False):
    x0, y0, x1, y1 = box
    if shadow:
        draw.rounded_rectangle((x0 + 10 * scale, y0 + 14 * scale, x1 + 10 * scale, y1 + 14 * scale),
                               radius=34 * scale, fill=(5, 26, 46))
    draw.rounded_rectangle(box, radius=32 * scale, fill=(250, 252, 255))
    # Folded page corner, kept plain to avoid a certificate or official-seal look.
    fold = 60 * scale
    draw.polygon([(x1 - fold, y0), (x1, y0 + fold), (x1 - fold, y0 + fold)], fill=(222, 235, 244))
    line_left = x0 + 42 * scale
    line_right = x1 - 42 * scale
    for y in (y0 + 104 * scale, y0 + 154 * scale, y0 + 204 * scale):
        draw.rounded_rectangle((line_left, y, line_right, y + 13 * scale),
                               radius=7 * scale, fill=(135, 161, 180))
    # A check mark is drawn directly on the page, not inside a badge.
    check_x = x0 + 130 * scale
    check_y = y0 + 287 * scale
    draw.line([(check_x - 54 * scale, check_y), (check_x - 10 * scale, check_y + 44 * scale),
               (check_x + 72 * scale, check_y - 48 * scale)],
              fill=(30, 166, 102), width=max(6, round(22 * scale)), joint="curve")


def render_icon():
    scale = 3
    size = 512 * scale
    image = gradient((size, size), (20, 73, 105), (21, 139, 111))
    draw = ImageDraw.Draw(image)
    draw_document(draw, (116 * scale, 67 * scale, 396 * scale, 445 * scale), scale=0.92 * scale, shadow=True)
    image = image.resize((512, 512), Image.Resampling.LANCZOS)
    image.convert("RGBA").save(ROOT / "icon-512.png", optimize=True)
    for density, dimension in (("mdpi", 48), ("hdpi", 72), ("xhdpi", 96), ("xxhdpi", 144), ("xxxhdpi", 192)):
        image.resize((dimension, dimension), Image.Resampling.LANCZOS).save(
            OUT / f"mipmap-{density}" / "ic_launcher.png", optimize=True
        )


def render_feature_graphic():
    scale = 2
    width, height = 1024 * scale, 500 * scale
    image = gradient((width, height), (16, 45, 66), (24, 83, 93))
    draw = ImageDraw.Draw(image)
    # Subtle decorative shapes are background-only and carry no official symbolism.
    draw.ellipse((680 * scale, -270 * scale, 1220 * scale, 270 * scale), fill=(30, 104, 107))
    draw.ellipse((-120 * scale, 340 * scale, 280 * scale, 740 * scale), fill=(20, 66, 85))
    draw_document(draw, (76 * scale, 58 * scale, 330 * scale, 438 * scale), scale=0.68 * scale, shadow=True)
    font_paths = [
        "/System/Library/Fonts/Supplemental/Arial Bold.ttf",
        "/System/Library/Fonts/Supplemental/Arial.ttf",
    ]
    font_bold = next((path for path in font_paths if "Bold" in path and Path(path).exists()), None)
    font_regular = next((path for path in font_paths if Path(path).exists()), None)
    title_font = ImageFont.truetype(font_bold, 52 * scale) if font_bold else ImageFont.load_default()
    body_font = ImageFont.truetype(font_regular, 27 * scale) if font_regular else ImageFont.load_default()
    text_x = 398 * scale
    draw.text((text_x, 142 * scale), "Validador de", font=title_font, fill=(255, 255, 255))
    draw.text((text_x, 208 * scale), "documentos", font=title_font, fill=(255, 255, 255))
    draw.rounded_rectangle((text_x, 302 * scale, text_x + 84 * scale, 310 * scale), radius=4 * scale,
                           fill=(86, 218, 151))
    draw.text((text_x, 342 * scale), "Verificação local de formato", font=body_font, fill=(213, 230, 238))
    draw.text((text_x, 382 * scale), "e dígitos verificadores", font=body_font, fill=(213, 230, 238))
    image.resize((1024, 500), Image.Resampling.LANCZOS).save(ROOT / "feature-graphic-1024x500.png", optimize=True)


if __name__ == "__main__":
    render_icon()
    render_feature_graphic()
