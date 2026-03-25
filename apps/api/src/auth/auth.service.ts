import { Injectable } from '@nestjs/common';
import { JwtService } from '@nestjs/jwt';

@Injectable()
export class AuthService {
  constructor(private readonly jwt: JwtService) {}

  signIn(userId: string, clubId: string, role: string) {
    return {
      accessToken: this.jwt.sign({ sub: userId, clubId, role })
    };
  }
}
