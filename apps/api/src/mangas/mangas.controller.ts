import { Controller, Get, Req, UseGuards } from '@nestjs/common';
import { JwtAuthGuard } from '../common/guards/jwt-auth.guard';
import { RolesGuard } from '../common/guards/roles.guard';
import { MangasService } from './mangas.service';

@Controller('mangas')
@UseGuards(JwtAuthGuard, RolesGuard)
export class MangasController {
  constructor(private readonly service: MangasService) {}

  @Get()
  list(@Req() req: { user: { clubId: string } }) {
    return this.service.list(req.user.clubId);
  }
}
