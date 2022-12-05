from Crypto.Util.number import long_to_bytes
from PIL import Image, ImageDraw
from random import getrandbits

width = 208
height = 208
flag = open('flag.txt').read()


def makeSourceImg():
    colors = long_to_bytes(getrandbits(width * height * 24))[::-1]
    img = Image.new('RGB', (width, height))
    x = 0
    for i in range(height):
        for j in range(width):
            img.putpixel((j, i), (colors[x], colors[x + 1], colors[x + 2]))
            x += 3
    return img


def makeFlagImg():
    img = Image.new("RGB", (width, height))
    draw = ImageDraw.Draw(img)
    draw.text((5, 5), flag, fill=(255, 255, 255))
    return img


if __name__ == '__main__':
    img1 = makeSourceImg()
    img2 = makeFlagImg()
    img3 = Image.new("RGB", (width, height))
    for i in range(height):
        for j in range(width):
            p1, p2 = img1.getpixel((j, i)), img2.getpixel((j, i))
            img3.putpixel((j, i), tuple([(p1[k] ^ p2[k]) for k in range(3)]))
    img3.save('attach.png')
