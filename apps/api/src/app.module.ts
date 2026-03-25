import { Module } from '@nestjs/common';
import { ConfigModule, ConfigService } from '@nestjs/config';
import { TypeOrmModule } from '@nestjs/typeorm';
import { AuthModule } from './auth/auth.module';
import { ClubsModule } from './clubs/clubs.module';
import { SeasonsModule } from './seasons/seasons.module';
import { CompetitionsModule } from './competitions/competitions.module';
import { MangasModule } from './mangas/mangas.module';
import { ParticipantsModule } from './participants/participants.module';
import { CatchesModule } from './catches/catches.module';
import { StandingsModule } from './standings/standings.module';
import { SyncModule } from './sync/sync.module';
import { RulesetsModule } from './rulesets/rulesets.module';

@Module({
  imports: [
    ConfigModule.forRoot({ isGlobal: true }),
    TypeOrmModule.forRootAsync({
      inject: [ConfigService],
      useFactory: (cfg: ConfigService) => ({
        type: 'postgres',
        url: cfg.get<string>('DATABASE_URL'),
        autoLoadEntities: true,
        synchronize: false
      })
    }),
    AuthModule,
    ClubsModule,
    SeasonsModule,
    CompetitionsModule,
    MangasModule,
    ParticipantsModule,
    CatchesModule,
    StandingsModule,
    SyncModule,
    RulesetsModule
  ]
})
export class AppModule {}
